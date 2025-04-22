package fer.hr.invsale.service;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.OrderReview;
import fer.hr.invsale.DTO.review.OrderReviewDTO;
import fer.hr.invsale.DTO.review.UpdateOrderReviewDTO;
import fer.hr.invsale.repository.InvsaleUserRepository;
import fer.hr.invsale.repository.OrderRepository;
import fer.hr.invsale.repository.OrderReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderReviewService {

    @Autowired
    OrderReviewRepository orderReviewRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<OrderReviewDTO> getAllOrderReviews() {
        return orderReviewRepository.findAll().stream().map(OrderReviewDTO::toDto).toList();
    }

    public OrderReviewDTO createOrderReview(OrderReviewDTO orderReview) throws InstanceAlreadyExistsException, NoSuchObjectException {
        validateExistence(orderReview);
        if(!uniqueConditionMet(orderReview))
            throw new InstanceAlreadyExistsException("User already has review for this order.");
        return OrderReviewDTO.toDto(createFromDto(orderReview));
    }

    private OrderReview createFromDto(OrderReviewDTO orderReview) {
        InvsaleUser invsaleUser = invsaleUserRepository.findById(orderReview.getInvsaleUser()).orElseThrow(NullPointerException::new);
        Order order = orderRepository.findById(orderReview.getOrderId()).orElseThrow(NullPointerException::new);
        OrderReview createOrderReview = new OrderReview();
        createOrderReview.setRating(orderReview.getRating());
        Optional.ofNullable(orderReview.getComment()).ifPresent(createOrderReview::setComment);
        createOrderReview.setReviewDate(orderReview.getReviewDate());
        createOrderReview.setInvsaleUser(invsaleUser);
        createOrderReview.setOrder(order);
        return orderReviewRepository.save(createOrderReview);
    }

    private boolean uniqueConditionMet(OrderReviewDTO orderReview) {
        return orderReviewRepository.findAll().stream().noneMatch(
                or -> or.getInvsaleUser().getEmail().equals(orderReview.getInvsaleUser())
                        && Objects.equals(or.getOrder().getIdOrder(), orderReview.getOrderId()));
    }

    private void validateExistence(OrderReviewDTO orderReview) throws NoSuchObjectException {
        if(!invsaleUserRepository.existsById(orderReview.getInvsaleUser()))
            throw new NoSuchObjectException("User with email " + orderReview.getInvsaleUser() + " does not exist.");
        if(!orderRepository.existsById(orderReview.getOrderId()))
            throw new NoSuchObjectException("Order with id " + orderReview.getOrderId() + " does not exist.");
    }

    public void updateOrderReview(UpdateOrderReviewDTO orderReview) throws NoSuchObjectException {
        if(!orderReviewRepository.existsById(orderReview.getIdReview()))
            throw new NoSuchObjectException("Review does not exist.");
        updateFromDto(orderReview);
    }

    private void updateFromDto(UpdateOrderReviewDTO orderReview) {
        OrderReview updateOrderReview = orderReviewRepository.findById(orderReview.getIdReview()).orElseThrow(NullPointerException::new);
        updateOrderReview.setComment(orderReview.getComment());
        orderReviewRepository.save(updateOrderReview);
    }

    public void deleteOrderReview(Integer id) throws NoSuchObjectException {
        if(!orderReviewRepository.existsById(id))
            throw new NoSuchObjectException("Order review with id " + id + " does not exist.");
        orderReviewRepository.deleteById(id);
    }

    public void deleteAllByOrder_IdOrder(Integer id) {
        orderReviewRepository.deleteAllByOrder_IdOrder(id);
    }
}
