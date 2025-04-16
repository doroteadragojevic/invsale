package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.order.CreateOrderDTO;
import fer.hr.invsale.DTO.order.OrderDTO;
import fer.hr.invsale.DTO.order.UpdateOrderDTO;
import fer.hr.invsale.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{email}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUser(@PathVariable String email) {
        try{
            return ResponseEntity.ok(orderService.getAllOrdersByUser(email));
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderDTO order) {
        try{
            return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateOrder( @RequestBody UpdateOrderDTO order) {
        try{
            orderService.updateOrder(order);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
