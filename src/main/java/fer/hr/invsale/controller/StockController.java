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

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("/shelves/{id}")
    public ResponseEntity<List<ShelfDTO>> getShelvesInStock(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(stockService.getShelvesInStock(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<StockDTO> createStock(@RequestBody StockDTO stock) {
        return new ResponseEntity<>(stockService.createStock(stock), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateStock(@RequestBody StockDTO stock) {
        try{
            stockService.updateStock(stock);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/stock/{id}/add/{shelfId}")
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

    @PutMapping("/stock/{id}/remove/{shelfId}")
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
