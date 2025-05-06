package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.review.OrderItemReviewDTO;
import fer.hr.invsale.DTO.review.UpdateOrderItemReviewDTO;
import fer.hr.invsale.service.OrderItemReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing order item reviews.
 * Provides endpoints to create, retrieve, update, and delete reviews related to individual order items.
 */
@RestController
@RequestMapping("/api/itemreviews")
public class OrderItemReviewController {

    @Autowired
    OrderItemReviewService orderItemReviewService;

    /**
     * Retrieves a review for a specific order item by its ID.
     *
     * @param id the ID of the order item review
     * @return ResponseEntity containing the review if found, or 404 NOT FOUND if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemReviewDTO> getOrderItemReviewById (@PathVariable Integer id) {
        return orderItemReviewService.getOrderItemReviewById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<OrderItemReviewDTO>> getOrderItemReviewByProductId (@PathVariable Integer id) {
        return ResponseEntity.ok(orderItemReviewService.getOrderItemReviewByProductId(id));
    }

    /**
     * Creates a new review for an order item.
     *
     * @param orderItemReview the review data to be created
     * @return ResponseEntity containing the created review and HTTP status 201 CREATED,
     *         or 409 CONFLICT if a review already exists for given order item by this user,
     *         or 400 BAD REQUEST if user or order item is not found
     */
    @PostMapping("/")
    public ResponseEntity<OrderItemReviewDTO> createOrderItemReview(@RequestBody OrderItemReviewDTO orderItemReview) {
        try{
            return new ResponseEntity<>(orderItemReviewService.createOrderItemReview(orderItemReview), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing review for an order item.
     *
     * @param orderItemReview the updated review data
     * @return ResponseEntity with HTTP status 204 NO CONTENT if update was successful,
     *         or 404 NOT FOUND if the review does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateOrderItemReview(@RequestBody UpdateOrderItemReviewDTO orderItemReview) {
        try{
            orderItemReviewService.updateOrderItemReview(orderItemReview);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a review for a specific order item by its ID.
     *
     * @param id the ID of the review to delete
     * @return ResponseEntity with HTTP status 204 NO CONTENT if deletion was successful,
     *         or 404 NOT FOUND if the review does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItemReview (@PathVariable Integer id) {
        try{
            orderItemReviewService.deleteOrderItemReview(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
