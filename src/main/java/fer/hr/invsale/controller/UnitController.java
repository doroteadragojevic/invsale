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

@RestController
@RequestMapping("/api/units")
public class UnitController {

    @Autowired
    UnitService unitService;

    @GetMapping("/")
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    @PostMapping("/")
    public ResponseEntity<UnitDTO> createUnit(@RequestBody UnitDTO unit) {
        return new ResponseEntity<>(unitService.createUnit(unit), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateUnit(@RequestBody UpdateUnitDTO unit) {
        try{
            unitService.updateUnit(unit);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
