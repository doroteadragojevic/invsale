package fer.hr.invsale.service;

import fer.hr.invsale.DAO.OrderStatus;
import fer.hr.invsale.DTO.orderStatus.OrderStatusDTO;
import fer.hr.invsale.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.Optional;

@Service
public class OrderStatusService {

    @Autowired
    OrderStatusRepository orderStatusRepository;

    public OrderStatusDTO createOrderStatus(OrderStatusDTO orderStatus) {
        if(orderStatusRepository.existsById(orderStatus.getName()))
            throw new KeyAlreadyExistsException("Order status " + orderStatus.getName() + " already exists.");
        return OrderStatusDTO.toDto(createFromDto(orderStatus));
    }

    private OrderStatus createFromDto(OrderStatusDTO orderStatus) {
        return orderStatusRepository.save(new OrderStatus(orderStatus.getName(), orderStatus.getDescription()));
    }

    public void updateOrderStatus(OrderStatusDTO orderStatus) throws NoSuchObjectException {
        if(!orderStatusRepository.existsById(orderStatus.getName()))
            throw new NoSuchObjectException("Order status " + orderStatus.getName() + " does not exist.");
        updateFromDto(orderStatus);
    }

    private void updateFromDto(OrderStatusDTO orderStatus) {
        OrderStatus updateOrderStatus = orderStatusRepository.findById(orderStatus.getName()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(orderStatus.getDescription()).ifPresent(updateOrderStatus::setDescription);
        orderStatusRepository.save(updateOrderStatus);
    }

    public void deleteOrderStatus(String name) throws NoSuchObjectException {
        if(!orderStatusRepository.existsById(name))
            throw new NoSuchObjectException("Order status " + name + " does not exist.");
        orderStatusRepository.deleteById(name);
    }
}
