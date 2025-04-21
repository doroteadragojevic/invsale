package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.priceList.PriceListDTO;
import fer.hr.invsale.DTO.priceList.UpdatePriceListDTO;
import fer.hr.invsale.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller that manages operations related to price lists.
 * Provides endpoints to create, retrieve, update, and delete price list entries.
 */
@RestController
@RequestMapping("/api/pricelist")
public class PriceListController {

    @Autowired
    PriceListService priceListService;

    /**
     * Retrieves all active price list entries.
     *
     * @return a list of {@link PriceListDTO} objects with HTTP 200 OK status
     */
    @GetMapping("/")
    public ResponseEntity<List<PriceListDTO>> getActivePriceLists() {
        return ResponseEntity.ok(priceListService.getActivePriceLists());
    }

    /**
     * Retrieves all active price list entries for a specific product.
     *
     * @param idProduct the ID of the product
     * @return a list of {@link PriceListDTO} objects with HTTP 200 OK status,
     *         or HTTP 404 Not Found if the product doesn't exist
     */
    @GetMapping("/{idProduct}")
    public ResponseEntity<List<PriceListDTO>> getActivePriceListsForProduct(@PathVariable Integer idProduct) {
        try{
            return ResponseEntity.ok(priceListService.getActivePriceListsForProduct(idProduct));
        }catch(NoSuchObjectException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Creates a new price list entry.
     *
     * @param priceList the {@link PriceListDTO} representing the new price list
     * @return the created {@link PriceListDTO} with HTTP 201 Created status,
     *         or HTTP 400 Bad Request if product does not contain unit, or product/unit does not exist
     *         / 409 Conflict if active price list with given unit and product already exists
     */
    @PostMapping("/")
    public ResponseEntity<PriceListDTO> createPriceList(@RequestBody PriceListDTO priceList) {
        try{
            return new ResponseEntity<>(priceListService.createPriceList(priceList), HttpStatus.CREATED);
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(NoSuchObjectException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Updates an existing price list entry.
     *
     * @param priceList the {@link UpdatePriceListDTO} containing updated data
     * @return HTTP 204 No Content if the update is successful,
     *         or appropriate error status (404, 400, 409) based on the exception
     */
    @PutMapping("/")
    public ResponseEntity<Void> updatePriceList(@RequestBody UpdatePriceListDTO priceList) {
        try{
            priceListService.updatePriceList(priceList);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Deletes a price list entry by its ID.
     *
     * @param id the ID of the price list to delete
     * @return HTTP 204 No Content if the deletion is successful,
     *         or HTTP 404 Not Found if the entry does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceList(@PathVariable Integer id) {
        try{
            priceListService.deletePriceList(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
