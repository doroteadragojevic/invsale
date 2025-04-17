package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.orderItem.OrderItemDTO;
import fer.hr.invsale.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing order items.
 * Provides endpoints to create, retrieve, update, and delete order items.
 */
@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService;

    /**
     * Retrieves all order items associated with a specific order.
     *
     * @param orderId the ID of the order
     * @return list of {@link OrderItemDTO} objects with HTTP status 200 if successful,
     * or 400 if the provided order ID is invalid
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItemsByOrderId(@PathVariable Integer orderId) {
        try{
            return ResponseEntity.ok(orderItemService.getAllOrderItemsByOrderId(orderId));
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves an order item by its ID.
     *
     * @param orderItemId the ID of the order item
     * @return the {@link OrderItemDTO} if found with HTTP status 200,
     * or 404 if not found
     */
    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Integer orderItemId) {
        return orderItemService.getOrderItemById(orderItemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new order item.
     *
     * @param orderItem the {@link OrderItemDTO} representing the order item to be created
     * @return the created {@link OrderItemDTO} with HTTP status 201 if successful,
     * 409 if the item already exists (same product and unit already exist in order),
     * or 400 if the input is invalid (order, product or unit does not exist)
     */
    @PostMapping("/")
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO orderItem) {
        try{
            return new ResponseEntity<>(orderItemService.createOrderItem(orderItem), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Increases the quantity of a specific order item by one.
     *
     * @param id the ID of the order item
     * @return HTTP status 204 if successful, or 404 if the item is not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> increaseQuantityByOne(@PathVariable Integer id) {
        try{
            orderItemService.increaseQuantityByOne(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param orderItemId the ID of the order item to delete
     * @return HTTP status 204 if successful, or 404 if the item is not found
     */
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItemById(@PathVariable Integer orderItemId) {
        try{
            orderItemService.deleteOrderItem(orderItemId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
