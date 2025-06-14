package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.review.OrderReviewDTO;
import fer.hr.invsale.DTO.review.UpdateOrderReviewDTO;
import fer.hr.invsale.service.OrderReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing reviews of entire orders.
 * Provides endpoints to create, retrieve, update, and delete order-level reviews.
 */
@RestController
@RequestMapping("/api/orderreview")
public class OrderReviewController {

    @Autowired
    OrderReviewService orderReviewService;

    /**
     * Retrieves all existing order reviews.
     *
     * @return ResponseEntity containing a list of all order reviews and HTTP status 200 OK
     */
    @GetMapping("/")
    public ResponseEntity<List<OrderReviewDTO>> getAllOrderReviews () {
        return ResponseEntity.ok(orderReviewService.getAllOrderReviews());
    }


    @GetMapping("/{idOrder}/{email}")
    public ResponseEntity<Boolean> orderReviewExists (@PathVariable Integer idOrder, @PathVariable String email) {
        return ResponseEntity.ok(orderReviewService.orderReviewExists(idOrder, email));
    }

    /**
     * Creates a new order review.
     *
     * @param orderReview the review data to be created
     * @return ResponseEntity containing the created review and HTTP status 201 CREATED,
     *         or 409 CONFLICT if a review already exists (review with given user and order id already exists),
     *         or 400 BAD REQUEST if user or order does not exist
     */
    @PostMapping("/")
    public ResponseEntity<OrderReviewDTO> createOrderReview(@RequestBody OrderReviewDTO orderReview) {
        try{
            return new ResponseEntity<>(orderReviewService.createOrderReview(orderReview), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing order review.
     *
     * @param orderReview the updated review data
     * @return ResponseEntity with HTTP status 204 NO CONTENT if update was successful,
     *         or 404 NOT FOUND if the review does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateOrderReview(@RequestBody UpdateOrderReviewDTO orderReview) {
        try{
            orderReviewService.updateOrderReview(orderReview);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an order review by its ID.
     *
     * @param id the ID of the review to be deleted
     * @return ResponseEntity with HTTP status 204 NO CONTENT if deletion was successful,
     *         or 404 NOT FOUND if the review does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderReview (@PathVariable Integer id) {
        try{
            orderReviewService.deleteOrderReview(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
