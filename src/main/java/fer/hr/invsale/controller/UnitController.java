package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.DTO.unit.UpdateUnitDTO;
import fer.hr.invsale.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing units of measurement.
 * Provides endpoints for retrieving, creating, updating, and deleting units.
 */
@RestController
@RequestMapping("/api/units")
public class UnitController {

    @Autowired
    UnitService unitService;

    /**
     * Retrieves all available units.
     *
     * @return a list of {@link UnitDTO} objects with HTTP status 200
     */
    @GetMapping("/")
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    /**
     * Creates a new unit.
     *
     * @param unit the {@link UnitDTO} containing unit details
     * @return the created {@link UnitDTO} with HTTP status 201
     */
    @PostMapping("/")
    public ResponseEntity<UnitDTO> createUnit(@RequestBody UnitDTO unit) {
        return new ResponseEntity<>(unitService.createUnit(unit), HttpStatus.CREATED);
    }

    /**
     * Updates an existing unit.
     *
     * @param unit the {@link UpdateUnitDTO} containing updated unit information
     * @return HTTP status 204 if successful, or 404 if the unit was not found
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateUnit(@RequestBody UpdateUnitDTO unit) {
        try{
            unitService.updateUnit(unit);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a unit by its ID.
     *
     * @param id the ID of the unit to delete
     * @return HTTP status 204 if successful, or 404 if the unit was not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Integer id) {
        try{
            unitService.deleteUnit(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
