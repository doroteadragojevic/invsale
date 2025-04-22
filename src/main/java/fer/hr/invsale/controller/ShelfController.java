package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.shelf.ShelfDTO;
import fer.hr.invsale.DTO.shelf.UpdateShelfDTO;
import fer.hr.invsale.DTO.shelfItem.ShelfItemDTO;
import fer.hr.invsale.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * Controller class responsible for handling HTTP requests related to shelf management.
 * Provides endpoints for creating, retrieving, updating, and deleting shelves,
 * as well as adding and removing items from shelves.
 */
@RestController
@RequestMapping("/api/shelf")
public class ShelfController {

    @Autowired
    ShelfService shelfService;

    /**
     * Retrieves all shelves.
     *
     * @return a list of all shelves wrapped in a {@link ResponseEntity} with status 200 OK
     */
    @GetMapping("/")
    public ResponseEntity<List<ShelfDTO>> getAllShelves() {
        return ResponseEntity.ok(shelfService.getAllShelves());
    }

    /**
     * Retrieves all items for a given shelf by ID.
     *
     * @param id the ID of the shelf
     * @return a list of shelf items with status 201 CREATED if found,
     *         or 404 NOT FOUND if the shelf does not exist
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<List<ShelfItemDTO>> getShelfItemsById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(shelfService.getShelfItemsById(id), HttpStatus.CREATED);
        } catch (NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Creates a new shelf.
     *
     * @param shelf the shelf data
     * @return the created shelf wrapped in a {@link ResponseEntity} with status 201 CREATED
     */
    @PostMapping("/")
    public ResponseEntity<ShelfDTO> createShelf(@RequestBody ShelfDTO shelf) {
        return new ResponseEntity<>(shelfService.createShelf(shelf), HttpStatus.CREATED);
    }

    /**
     * Updates an existing shelf.
     *
     * @param shelf the shelf data to update
     * @return status 204 NO CONTENT if successful, 404 NOT FOUND if the shelf does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateShelf(@RequestBody UpdateShelfDTO shelf) {
        try{
            shelfService.updateShelf(shelf);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds an item to a shelf.
     *
     * @param shelfId the ID of the shelf
     * @param itemId the ID of the item to add
     * @return status 204 NO CONTENT if successful,
     *         404 NOT FOUND if the shelf does not exist,
     *         or 400 BAD REQUEST if the item does not exist
     */
    @PutMapping("/{shelfId}/add/{itemId}")
    public ResponseEntity<Void> addShelfItem(@PathVariable Integer shelfId, @PathVariable Integer itemId) {
        try{
            shelfService.addShelfItem(shelfId, itemId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Removes an item from a shelf.
     *
     * @param shelfId the ID of the shelf
     * @param itemId the ID of the item to remove
     * @return status 204 NO CONTENT if successful,
     *         404 NOT FOUND if the shelf does not exist,
     *         or 400 BAD REQUEST if the item does not exist
     */
    @PutMapping("/{shelfId}/remove/{itemId}")
    public ResponseEntity<Void> removeShelfItem(@PathVariable Integer shelfId, @PathVariable Integer itemId) {
        try{
            shelfService.removeShelfItem(shelfId, itemId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a shelf by ID.
     *
     * @param id the ID of the shelf to delete
     * @return status 204 NO CONTENT if successful, 404 NOT FOUND if the shelf does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelf(@PathVariable Integer id) {
        try{
            shelfService.deleteShelf(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
