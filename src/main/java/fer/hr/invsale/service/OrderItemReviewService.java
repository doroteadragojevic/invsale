package fer.hr.invsale.service;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.OrderItem;
import fer.hr.invsale.DAO.OrderItemReview;
import fer.hr.invsale.DTO.review.OrderItemReviewDTO;
import fer.hr.invsale.DTO.review.UpdateOrderItemReviewDTO;
import fer.hr.invsale.repository.InvsaleUserRepository;
import fer.hr.invsale.repository.OrderItemRepository;
import fer.hr.invsale.repository.OrderItemReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderItemReviewService {

    @Autowired
    OrderItemReviewRepository orderItemReviewRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Optional<OrderItemReviewDTO> getOrderItemReviewById(Integer id) {
        return orderItemReviewRepository.findById(id).map(OrderItemReviewDTO::toDto);
    }

    public OrderItemReviewDTO createOrderItemReview(OrderItemReviewDTO orderItemReview) throws NoSuchObjectException, InstanceAlreadyExistsException {
        validateExistence(orderItemReview);
        if(!uniqueConditionMet(orderItemReview))
            throw new InstanceAlreadyExistsException("User already has review for this order item.");
        return OrderItemReviewDTO.toDto(createFromDto(orderItemReview));
    }

    private OrderItemReview createFromDto(OrderItemReviewDTO orderItemReview) {
        OrderItemReview createOrderItemReview = new OrderItemReview();
        createOrderItemReview.setRating(orderItemReview.getRating());
        Optional.ofNullable(orderItemReview.getComment()).ifPresent(createOrderItemReview::setComment);
        createOrderItemReview.setReviewDate(orderItemReview.getReviewDate());
        InvsaleUser user = invsaleUserRepository.findById(orderItemReview.getInvsaleUser()).orElseThrow(NullPointerException::new);
        createOrderItemReview.setInvsaleUser(user);
        OrderItem orderItem = orderItemRepository.findById(orderItemReview.getOrderItemId()).orElseThrow(NullPointerException::new);
        createOrderItemReview.setOrderItem(orderItem);
        return orderItemReviewRepository.save(createOrderItemReview);
    }

    private boolean uniqueConditionMet(OrderItemReviewDTO orderItemReview) {
        return orderItemReviewRepository.findAll().stream().noneMatch(
                oir -> Objects.equals(oir.getOrderItem().getIdOrderItem(), orderItemReview.getOrderItemId())
                        && oir.getInvsaleUser().getEmail().equals(orderItemReview.getInvsaleUser()));
    }

    private void validateExistence(OrderItemReviewDTO orderItemReview) throws NoSuchObjectException {
        if(!invsaleUserRepository.existsById(orderItemReview.getInvsaleUser()) ||
                !orderItemRepository.existsById(orderItemReview.getOrderItemId()))
            throw new NoSuchObjectException("User or order item does not exist.");
    }

    public void updateOrderItemReview(UpdateOrderItemReviewDTO orderItemReview) throws NoSuchObjectException {
        if(!orderItemReviewRepository.existsById(orderItemReview.getIdReview()))
            throw new NoSuchObjectException("Review does not exist.");
        updateFromDto(orderItemReview);
    }

    private void updateFromDto(UpdateOrderItemReviewDTO orderItemReview) {
        OrderItemReview updateOrderItemReview = orderItemReviewRepository.findById(orderItemReview.getIdReview()).orElseThrow(NullPointerException::new);
        updateOrderItemReview.setComment(orderItemReview.getComment());
        orderItemReviewRepository.save(updateOrderItemReview);
    }

    public void deleteOrderItemReview(Integer id) throws NoSuchObjectException {
        if(!orderItemReviewRepository.existsById(id))
            throw new NoSuchObjectException("Order item review with id " + id + " does not exist.");
        orderItemReviewRepository.deleteById(id);
    }

    public void deleteAllByOrderItemId(Integer orderItemId) {
        orderItemReviewRepository.deleteAllByOrderItem_IdOrderItem(orderItemId);
    }

    public List<OrderItemReviewDTO> getReviewsForProduct(Integer id) {
        return orderItemReviewRepository.findAll()
                .stream()
                .filter(oir -> Objects.equals(oir.getOrderItem().getProduct().getIdProduct(), id))
                .map(OrderItemReviewDTO::toDto).toList();
    }

    public List<OrderItemReviewDTO> getOrderItemReviewByProductId(Integer id) {
        return orderItemReviewRepository.findAll().stream().filter(orderItemReview -> orderItemReview.getOrderItem().getProduct().getIdProduct() == id).map(OrderItemReviewDTO::toDto).toList();

    }
}
