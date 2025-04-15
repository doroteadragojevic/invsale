package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Manufacturer;
import fer.hr.invsale.DTO.manufacturer.CreateManufacturerDTO;
import fer.hr.invsale.DTO.manufacturer.ManufacturerDTO;
import fer.hr.invsale.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public List<ManufacturerDTO> getAllManufacturers() {
        return manufacturerRepository.findAll().stream().map(ManufacturerDTO::toDto).toList();
    }

    public Optional<ManufacturerDTO> getManufacturerById(Integer id) {
        return manufacturerRepository.findById(id).map(ManufacturerDTO::toDto);
    }

    public ManufacturerDTO createManufacturer(CreateManufacturerDTO manufacturer) {
        return ManufacturerDTO.toDto(manufacturerRepository.save(new Manufacturer(manufacturer.getName())));
    }

    public void updateManufacturer(ManufacturerDTO manufacturer) throws NoSuchObjectException {
        if (!manufacturerRepository.existsById(manufacturer.getIdManufacturer()))
            throw new NoSuchObjectException("Manufacturer with id " + manufacturer.getIdManufacturer() + " does not exist.");
        updateFromDto(manufacturer);
    }

    private void updateFromDto(ManufacturerDTO manufacturer) {
        Manufacturer dbManufacturer = manufacturerRepository.findById(manufacturer.getIdManufacturer())
                .orElseThrow(() -> new NullPointerException("Manufacturer does not exist."));
        dbManufacturer.setName(manufacturer.getName());
        manufacturerRepository.save(dbManufacturer);
    }

    public void deleteManufacturer(Integer id) throws NoSuchObjectException {
        if(!manufacturerRepository.existsById(id))
            throw new NoSuchObjectException("Manufacturer with id " + id + " does not exist.");
        manufacturerRepository.deleteById(id);
    }
}
