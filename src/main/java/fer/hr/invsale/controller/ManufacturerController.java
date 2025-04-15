package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.manufacturer.CreateManufacturerDTO;
import fer.hr.invsale.DTO.manufacturer.ManufacturerDTO;
import fer.hr.invsale.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;

    @GetMapping("/")
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerService.getAllManufacturers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable Integer id) {
        return manufacturerService.getManufacturerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<ManufacturerDTO> createManufacturer(@RequestBody CreateManufacturerDTO manufacturer) {
        return new ResponseEntity<>(manufacturerService.createManufacturer(manufacturer), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateManufacturer(@RequestBody ManufacturerDTO manufacturer) {
        try{
            manufacturerService.updateManufacturer(manufacturer);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturerById(@PathVariable Integer id) {
        try{
            manufacturerService.deleteManufacturer(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
