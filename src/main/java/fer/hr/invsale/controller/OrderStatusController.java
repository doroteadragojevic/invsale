package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.orderStatus.OrderStatusDTO;
import fer.hr.invsale.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;

/**
 * REST controller for managing order status entities.
 * <p>
 * Provides endpoints for creating, updating, and deleting order statuses.
 */
@RestController
@RequestMapping("/api/orderstatus")
public class OrderStatusController {

    @Autowired
    OrderStatusService orderStatusService;

    /**
     * Creates a new order status.
     *
     * @param orderStatus the {@link OrderStatusDTO} containing the name and details of the order status to be created
     * @return the created {@link OrderStatusDTO} with HTTP 201 if successful,
     *         or HTTP 409 if an order status with the same name already exists
     */
    @PostMapping("/")
    public ResponseEntity<OrderStatusDTO> createOrderStatus(@RequestBody OrderStatusDTO orderStatus) {
        try{
            return new ResponseEntity<>(orderStatusService.createOrderStatus(orderStatus), HttpStatus.CREATED);
        }catch(KeyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Updates an existing order status description.
     *
     * @param orderStatus the {@link OrderStatusDTO} containing updated data
     * @return HTTP 204 (No Content) if the update was successful,
     *         or HTTP 404 if the order status does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateOrderStatus(@RequestBody OrderStatusDTO orderStatus) {
        try{
            orderStatusService.updateOrderStatus(orderStatus);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an order status by its name.
     *
     * @param name the name of the order status to delete
     * @return HTTP 204 (No Content) if the deletion was successful,
     *         or HTTP 404 if no such order status exists
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable String name) {
        try{
            orderStatusService.deleteOrderStatus(name);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
