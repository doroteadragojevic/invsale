package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.shelfItem.ShelfItemDTO;
import fer.hr.invsale.DTO.shelfItem.UpdateShelfItemDTO;
import fer.hr.invsale.service.ShelfItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;

/**
 * REST controller for managing shelf items.
 * Provides endpoints for CRUD operations on shelf items.
 */
@RestController
@RequestMapping("/api/shelfitem")
public class ShelfItemController {

    @Autowired
    ShelfItemService shelfItemService;

    /**
     * Retrieves a shelf item by its ID.
     *
     * @param id the ID of the shelf item
     * @return the shelf item if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShelfItemDTO> getShelfItemById(@PathVariable Integer id) {
        return shelfItemService.getShelfItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new shelf item.
     *
     * @param shelfItem the shelf item to create
     * @return the created shelf item and HTTP status 201 Created if successful,
     *         409 Conflict if the item with given product and unit already exist,
     *         or 400 Bad Request if product/unit does not exist or product does not contain given unit
     */
    @PostMapping("/")
    public ResponseEntity<ShelfItemDTO> createShelfItem(@RequestBody ShelfItemDTO shelfItem) {
        try{
            return new ResponseEntity<>(shelfItemService.createShelfItem(shelfItem), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing shelf item.
     *
     * @param shelfItem the updated shelf item data
     * @return HTTP status 204 No Content if successful,
     *         or 404 Not Found if the item does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateShelfItem(@RequestBody UpdateShelfItemDTO shelfItem) {
        try{
            shelfItemService.updateShelfItem(shelfItem);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a shelf item by its ID.
     *
     * @param id the ID of the shelf item to delete
     * @return HTTP status 204 No Content if deleted,
     *         or 404 Not Found if the item does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelfItem(@PathVariable Integer id) {
        try{
            shelfItemService.deleteShelfItem(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
