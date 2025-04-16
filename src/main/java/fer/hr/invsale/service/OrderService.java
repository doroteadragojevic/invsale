package fer.hr.invsale.service;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.OrderStatus;
import fer.hr.invsale.DTO.order.CreateOrderDTO;
import fer.hr.invsale.DTO.order.OrderDTO;
import fer.hr.invsale.DTO.order.UpdateOrderDTO;
import fer.hr.invsale.repository.InvsaleUserRepository;
import fer.hr.invsale.repository.OrderRepository;
import fer.hr.invsale.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    public List<OrderDTO> getAllOrdersByUser(String email) {
        return orderRepository.findAllByInvsaleUser_Email(email).stream().map(OrderDTO::toDto).toList();
    }

    public Optional<OrderDTO> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).map(OrderDTO::toDto);
    }

    public OrderDTO createOrder(CreateOrderDTO order) throws InstanceAlreadyExistsException {
        checkIfOrderInProgressExists(order);
        return createFromDto(order);
    }

    private OrderDTO createFromDto(CreateOrderDTO order) {
        Order createdOrder = new Order();
        OrderStatus orderStatus = orderStatusRepository.findById("IN_PROGRESS").orElseThrow(NullPointerException::new);
        createdOrder.setOrderStatus(orderStatus);
        InvsaleUser invsaleUser = invsaleUserRepository.findById(order.getEmail()).orElseThrow(NullPointerException::new);
        createdOrder.setInvsaleUser(invsaleUser);
        createdOrder.setPaymentMethod(order.getPaymentMethod());
        createdOrder.setShippingAddress(order.getShippingAddress());
        createdOrder.setOrderTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        createdOrder.setTotalPrice(0.);
        return OrderDTO.toDto(
                orderRepository.save(createdOrder));

    }

    private void checkIfOrderInProgressExists(CreateOrderDTO order) throws InstanceAlreadyExistsException {
        orderRepository.findAllByInvsaleUser_Email(order.getEmail())
                .stream()
                .filter(o -> o.getOrderStatus().getName().equals("IN_PROGRESS"))
                .findAny()
                .ifPresent(o ->
                        new InstanceAlreadyExistsException("This user already has order in progress.")
                );

    }

    public void updateOrder(UpdateOrderDTO order) throws NoSuchObjectException {
        if(!orderRepository.existsById(order.getIdOrder()))
            throw new NoSuchObjectException("Order with id " + order.getIdOrder() + " does not exist.");
        updateFromDto(order);
    }

    private void updateFromDto(UpdateOrderDTO order) {
        Order updateOrder = orderRepository.findById(order.getIdOrder()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(order.getPaymentMethod()).ifPresent(updateOrder::setPaymentMethod);
        Optional.ofNullable(order.getShippingAddress()).ifPresent(updateOrder::setShippingAddress);
        orderRepository.save(updateOrder);

    }

    public void deleteOrder(Integer id) throws NoSuchObjectException {
        if(!orderRepository.existsById(id))
            throw new NoSuchObjectException("Object with id " + id + " does not exist.");
        orderRepository.deleteById(id);
    }
}
