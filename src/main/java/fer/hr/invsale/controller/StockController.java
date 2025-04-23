package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.shelf.ShelfDTO;
import fer.hr.invsale.DTO.stock.StockDTO;
import fer.hr.invsale.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing stocks and their related shelves.
 * Provides endpoints for CRUD operations on stock entities and for managing shelf associations.
 */
@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    StockService stockService;

    /**
     * Retrieves all shelves assigned to a specific stock.
     *
     * @param id the ID of the stock
     * @return list of shelves in the given stock or 404 Not Found if the stock does not exist
     */
    @GetMapping("/shelves/{id}")
    public ResponseEntity<List<ShelfDTO>> getShelvesInStock(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(stockService.getShelvesInStock(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new stock.
     *
     * @param stock the stock data to be created
     * @return the created stock with HTTP status 201 Created
     */
    @PostMapping("/")
    public ResponseEntity<StockDTO> createStock(@RequestBody StockDTO stock) {
        return new ResponseEntity<>(stockService.createStock(stock), HttpStatus.CREATED);
    }

    /**
     * Updates an existing stock.
     *
     * @param stock the updated stock data
     * @return HTTP status 204 No Content if update is successful, or 404 Not Found if the stock does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateStock(@RequestBody StockDTO stock) {
        try{
            stockService.updateStock(stock);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a shelf to the specified stock.
     *
     * @param id the ID of the stock
     * @param shelfId the ID of the shelf to add
     * @return HTTP status 204 No Content if successful,
     *         404 Not Found if the stock does not exist,
     *         or 400 Bad Request if the shelf does not exist
     */
    @PutMapping("/{id}/add/{shelfId}")
    public ResponseEntity<Void> addShelf(@PathVariable Integer id, @PathVariable Integer shelfId) {
        try{
            stockService.addShelf(id, shelfId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Removes a shelf from the specified stock.
     *
     * @param id the ID of the stock
     * @param shelfId the ID of the shelf to remove
     * @return HTTP status 204 No Content if successful,
     *         404 Not Found if the stock does not exist,
     *         or 400 Bad Request if the shelf does not exist
     */
    @PutMapping("/{id}/remove/{shelfId}")
    public ResponseEntity<Void> removeShelf(@PathVariable Integer id, @PathVariable Integer shelfId) {
        try{
            stockService.removeShelf(id, shelfId);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a stock by its ID.
     *
     * @param id the ID of the stock to delete
     * @return HTTP status 204 No Content if deleted, or 404 Not Found if the stock does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Integer id) {
        try{
            stockService.deleteStock(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
