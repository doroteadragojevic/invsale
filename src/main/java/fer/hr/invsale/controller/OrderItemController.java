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

@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItemsByOrderId(@PathVariable Integer orderId) {
        try{
            return ResponseEntity.ok(orderItemService.getAllOrderItemsByOrderId(orderId));
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Integer orderItemId) {
        return orderItemService.getOrderItemById(orderItemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> increaseQuantityByOne(@PathVariable Integer id) {
        try{
            orderItemService.increaseQuantityByOne(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItemById(@PathVariable Integer orderItemId) {
        try{
            orderItemService.deleteOrderItem(orderItemId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE ALL ORDER ITEMS WHEN ORDER IS DELETED

}
