package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.ShelfItem;
import fer.hr.invsale.DAO.Unit;
import fer.hr.invsale.DTO.shelfItem.ShelfItemDTO;
import fer.hr.invsale.DTO.shelfItem.UpdateShelfItemDTO;
import fer.hr.invsale.repository.ProductRepository;
import fer.hr.invsale.repository.ShelfItemRepository;
import fer.hr.invsale.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.Optional;

@Service
public class ShelfItemService {

    @Autowired
    ShelfItemRepository shelfItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UnitRepository unitRepository;

    public Optional<ShelfItemDTO> getShelfItemById(Integer id) {
        return shelfItemRepository.findById(id).map(ShelfItemDTO::toDto);
    }

    public ShelfItemDTO createShelfItem(ShelfItemDTO shelfItem) throws InstanceAlreadyExistsException {
        validateExistence(shelfItem);
        if (!uniqueConditionMet(shelfItem))
            throw new InstanceAlreadyExistsException("Shelf item with given product and unit already exists.");
        return ShelfItemDTO.toDto(createFromDto(shelfItem));
    }

    private ShelfItem createFromDto(ShelfItemDTO shelfItem) {
        Product product = productRepository.findById(shelfItem.getProductId()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(shelfItem.getUnitId()).orElseThrow(NullPointerException::new);
        ShelfItem createShelfItem = new ShelfItem();
        createShelfItem.setDimensionX(shelfItem.getDimensionX());
        createShelfItem.setDimensionY(shelfItem.getDimensionY());
        createShelfItem.setProduct(product);
        createShelfItem.setUnit(unit);
        return shelfItemRepository.save(createShelfItem);
    }

    private boolean uniqueConditionMet(ShelfItemDTO shelfItem) {
        Product product = productRepository.findById(shelfItem.getProductId()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(shelfItem.getUnitId()).orElseThrow(NullPointerException::new);
        return shelfItemRepository.findByProductAndUnit(product, unit).isEmpty();
    }

    private void validateExistence(ShelfItemDTO shelfItem) {
        if (!productRepository.existsById(shelfItem.getProductId())
                || !unitRepository.existsById(shelfItem.getUnitId()))
            throw new IllegalArgumentException("Product/unit does not exist.");
        Product product = productRepository.findById(shelfItem.getProductId()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(shelfItem.getUnitId()).orElseThrow(NullPointerException::new);
        if(!product.getQuantityUnits().contains(unit))
            throw new IllegalArgumentException("Product does not contain this unit.");
    }

    public void updateShelfItem(UpdateShelfItemDTO shelfItem) throws NoSuchObjectException {
        if (shelfItemRepository.findById(shelfItem.getIdShelfItem()).isPresent()){
            ShelfItem updateShelfItem = shelfItemRepository.findById(shelfItem.getIdShelfItem()).get();
            Optional.ofNullable(shelfItem.getDimensionX()).ifPresent(updateShelfItem::setDimensionX);
            Optional.ofNullable(shelfItem.getDimensionY()).ifPresent(updateShelfItem::setDimensionY);
            shelfItemRepository.save(updateShelfItem);
        }
        throw new NoSuchObjectException("Shelf item with id " + shelfItem.getIdShelfItem() + " does not exist.");
    }

    public void deleteShelfItem(Integer id) throws NoSuchObjectException {
        if(!shelfItemRepository.existsById(id))
            throw new NoSuchObjectException("Shelf item with id " + id + " does not exist.");
        shelfItemRepository.deleteById(id);
    }
}
