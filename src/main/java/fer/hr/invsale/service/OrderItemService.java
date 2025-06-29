package fer.hr.invsale.service;

import fer.hr.invsale.DAO.*;
import fer.hr.invsale.DTO.orderItem.OrderItemDTO;
import fer.hr.invsale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemReviewService orderItemReviewService;

    @Autowired
    PriceListRepository priceListRepository;

    public List<OrderItemDTO> getAllOrderItemsByOrderId(Integer orderId) {
        if (!orderRepository.existsById(orderId))
            throw new IllegalArgumentException("Order with id " + orderId + " does not exist.");
        return orderItemRepository.findAllByOrder_IdOrder(orderId).stream().map(OrderItemDTO::toDto).toList();
    }

    public Optional<OrderItemDTO> getOrderItemById(Integer orderItemId) {
        return orderItemRepository.findById(orderItemId).map(OrderItemDTO::toDto);
    }

    /**
     * Create new order item.
     * Checks if given order, unit and product exist.
     * Checks if NEW order item meet uniqueness condition, meaning that other order item with same unit and product
     * should not exist in current order.
     *
     * @param orderItem
     * @return OrderItemDTO
     * @throws InstanceAlreadyExistsException if order item with same unit and product already exists for current order
     * @throws IllegalArgumentException       if order, unit or product does not exist
     */
    public OrderItemDTO createOrderItem(OrderItemDTO orderItem) throws InstanceAlreadyExistsException {
        validateExistence(orderItem);
        if (!uniqueConditionMet(orderItem))
            throw new InstanceAlreadyExistsException("Order item with given product and unit already exists in order.");
        return OrderItemDTO.toDto(createFromDto(orderItem));
    }

    private OrderItem createFromDto(OrderItemDTO orderItem) {
        Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(orderItem.getUnitId()).orElseThrow(NullPointerException::new);
        Order order = orderRepository.findById(orderItem.getOrderId()).orElseThrow(NullPointerException::new);
        OrderItem newOrderItem = new OrderItem(product, unit, orderItem.getQuantity(), order);
        newOrderItem = orderItemRepository.save(newOrderItem);
        updateOrderTotalPrice(newOrderItem.getIdOrderItem(), true, 1);
        reservationService.reserveProduct(product.getIdProduct(), order.getInvsaleUser().getEmail(), orderItem.getQuantity(), orderItem.getUnitId(), orderItem.getOrderId());
        return newOrderItem;
    }

    private Boolean uniqueConditionMet(OrderItemDTO uniqueOrderItem) {
        Product product = productRepository.findById(uniqueOrderItem.getProductId()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(uniqueOrderItem.getUnitId()).orElseThrow(NullPointerException::new);
        Order order = orderRepository.findById(uniqueOrderItem.getOrderId()).orElseThrow(NullPointerException::new);
        return orderItemRepository.findByProductAndUnitAndOrder(product, unit, order).isEmpty();
    }

    private void validateExistence(OrderItemDTO orderItem) {
        if (!orderRepository.existsById(orderItem.getOrderId())
                || !unitRepository.existsById(orderItem.getUnitId())
                || !productRepository.existsById(orderItem.getProductId())
        )
            throw new IllegalArgumentException("Not valid order/unit/product id.");
    }

    public void increaseQuantityByOne(Integer id) throws NoSuchObjectException {
        if (!orderItemRepository.existsById(id))
            throw new NoSuchObjectException("Order item with id " + id + " does not exist.");
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(NullPointerException::new);
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        updateOrderTotalPrice(id, true, 1);
        reservationService.updateReservation(orderItem.getProduct().getIdProduct(), orderItem.getOrder().getInvsaleUser().getEmail(), 1, orderItem.getUnit().getIdUnit());
        orderItemRepository.save(orderItem);
    }

    private void updateOrderTotalPrice(Integer id, Boolean increase, Integer quantity) {
        OrderItem orderItem = orderItemRepository.findById(id).get();
        Order order = orderItem.getOrder();
        PriceList priceList = priceListRepository.findByProductAndUnit(orderItem.getProduct(), orderItem.getUnit()).get();
        Double priceWithDiscount = (priceList.getDiscount() == null || priceList.getDiscount() == 0) ?
                priceList.getPriceWithoutDiscount() :
                priceList.getPriceWithoutDiscount() * (1 - priceList.getDiscount());
        int operationType = increase ? 1 : (-1);
        order.setTotalPrice(order.getTotalPrice() + (priceWithDiscount * operationType * quantity));
        orderRepository.save(order);
    }

    public void deleteOrderItem(Integer orderItemId) throws NoSuchObjectException {
        if (!orderItemRepository.existsById(orderItemId))
            throw new NoSuchObjectException("Order item with id " + orderItemId + " does not exist.");
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Product product = orderItem.getProduct();
        InvsaleUser user = orderItem.getOrder().getInvsaleUser();
        reservationService.deleteReservation(product.getIdProduct(), user.getEmail(), orderItem.getUnit().getIdUnit());
        updateOrderTotalPrice(orderItemId, false, orderItem.getQuantity());
        orderItemReviewService.deleteAllByOrderItemId(orderItemId);
        orderItemRepository.deleteById(orderItemId);
    }

    public void decreaseQuantityByOne(Integer id) throws NoSuchObjectException {
        if (!orderItemRepository.existsById(id))
            throw new NoSuchObjectException("Order item with id " + id + " does not exist.");
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(NullPointerException::new);
        if (orderItem.getQuantity() != 0) {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            updateOrderTotalPrice(id, false, 1);
            reservationService.updateReservation(orderItem.getProduct().getIdProduct(), orderItem.getOrder().getInvsaleUser().getEmail(), -1, orderItem.getUnit().getIdUnit());
            orderItemRepository.save(orderItem);
        }
        if (orderItem.getQuantity() == 0){
            updateOrderTotalPrice(id, false, 1);
            reservationService.deleteReservation(orderItem.getProduct().getIdProduct(), orderItem.getOrder().getInvsaleUser().getEmail(), orderItem.getUnit().getIdUnit());
            orderItemRepository.deleteById(id);
        }

    }
}
