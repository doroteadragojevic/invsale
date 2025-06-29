package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.StatsDTO;
import fer.hr.invsale.DTO.order.CreateOrderDTO;
import fer.hr.invsale.DTO.order.OrderDTO;
import fer.hr.invsale.DTO.order.UpdateOrderDTO;
import fer.hr.invsale.DTO.stock.WarehouseStatsDTO;
import fer.hr.invsale.DTO.stock.ZoneProductsDTO;
import fer.hr.invsale.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing orders.
 * <p>
 * This controller provides endpoints for performing CRUD operations on orders,
 * including creating, retrieving, updating, and deleting orders.
 * </p>
 *
 * <p>
 * Example routes:
 * <ul>
 *     <li><b>GET</b> /api/orders/{email} – Retrieves all orders for a specific user</li>
 *     <li><b>GET</b> /api/orders/id/{orderId} – Retrieves a specific order by ID</li>
 *     <li><b>POST</b> /api/orders/ – Creates a new order</li>
 *     <li><b>PUT</b> /api/orders/ – Updates an existing order</li>
 *     <li><b>DELETE</b> /api/orders/{id} – Deletes an order by ID</li>
 * </ul>
 * </p>
 *
 * @author Dorotea Dragojević
 * @version 1.0
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param email the email of the user whose orders are being requested
     * @return list of {@link OrderDTO} wrapped in a {@link ResponseEntity} or 400 Bad Request if user does not exist
     */
    @GetMapping("/{email}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUser(@PathVariable String email) {
        try{
            return ResponseEntity.ok(orderService.getAllOrdersByUser(email));
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return {@link OrderDTO} wrapped in a {@link ResponseEntity}, or 404 if not found
     */
    @GetMapping("/id/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cart/{email}")
    public ResponseEntity<OrderDTO> getCart(@PathVariable String email) {
        return orderService.getCart(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/{range}")
    public ResponseEntity<List<StatsDTO>> getStatsInRange(@PathVariable String range, @RequestParam(required = false) String category,
                                                          @RequestParam(required = false) Integer product) {
        return orderService.getStatsInRange(range, category, product);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{idOrder}/apply/{code}")
    public ResponseEntity<OrderDTO> applyCoupon(@PathVariable Integer idOrder, @PathVariable String code) {
        try{
            return new ResponseEntity<>(orderService.applyCoupon(idOrder, code), HttpStatus.CREATED);
        }catch(NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{idOrder}/remove/{code}")
    public ResponseEntity<OrderDTO> removeCoupon(@PathVariable Integer idOrder, @PathVariable String code) {
        try{
            return new ResponseEntity<>(orderService.removeCoupon(idOrder, code), HttpStatus.CREATED);
        }catch(NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Creates a new order.
     *
     * @param order the {@link CreateOrderDTO} containing order details
     * @return the created {@link OrderDTO} with HTTP status 201 if successful,
     * or 409 if an order in progress already exists for the user
     */
    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderDTO order) {
        try{
            return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/place/")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody UpdateOrderDTO order) {
        try{
            return new ResponseEntity<>(orderService.placeOrder(order), HttpStatus.CREATED);
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing order.
     *
     * @param order the {@link UpdateOrderDTO} containing updated order details
     * @return HTTP 204 if successful, or 404 if the order does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateOrder( @RequestBody UpdateOrderDTO order) {
        try{
            orderService.updateOrder(order);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idOrder}/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer idOrder, @PathVariable String status) {
        try{
            orderService.updateStatus(idOrder, status);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     * @return HTTP 204 if deleted successfully, or 404 if the order does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/warehouse/stats")
    public ResponseEntity<List<WarehouseStatsDTO>> getWarehouseStats() {
        return ResponseEntity.ok(orderService.getWarehouseStats());
    }

    @GetMapping("/warehouse/zones")
    public ResponseEntity<List<ZoneProductsDTO>> getZoneProducts() {
        return ResponseEntity.ok(orderService.getZoneProducts());
    }

}
