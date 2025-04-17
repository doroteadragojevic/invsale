package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Unit;
import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.DTO.unit.UpdateUnitDTO;
import fer.hr.invsale.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    @Autowired
    UnitRepository unitRepository;

    public List<UnitDTO> getAllUnits() {
        return unitRepository.findAll().stream().map(UnitDTO::toDto).toList();
    }

    public UnitDTO createUnit(UnitDTO unit) {
        return UnitDTO.toDto(
                unitRepository.save(new Unit(unit.getName(), unit.getDescription()))
        );
    }

    public void updateUnit(UpdateUnitDTO unit) throws NoSuchObjectException {
        if(!unitRepository.existsById(unit.getIdUnit()))
            throw new NoSuchObjectException("Unit with id " + unit.getIdUnit() + " does not exist.");
        updateFromDto(unit);
    }

    private void updateFromDto(UpdateUnitDTO unit) {
        Unit updateUnit = unitRepository.findById(unit.getIdUnit()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(unit.getName()).ifPresent(updateUnit::setName);
        Optional.ofNullable(unit.getDescription()).ifPresent(updateUnit::setDescription);
        unitRepository.save(updateUnit);
    }

    public void deleteUnit(Integer id) throws NoSuchObjectException {
        if(!unitRepository.existsById(id))
            throw new NoSuchObjectException("Unit with id " + id + " does not exist.");
        unitRepository.deleteById(id);
    }
}
