package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Shelf;
import fer.hr.invsale.DAO.ShelfItem;
import fer.hr.invsale.DTO.shelf.ShelfDTO;
import fer.hr.invsale.DTO.shelf.UpdateShelfDTO;
import fer.hr.invsale.DTO.shelfItem.ShelfItemDTO;
import fer.hr.invsale.repository.ShelfItemRepository;
import fer.hr.invsale.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShelfService {

    @Autowired
    ShelfRepository shelfRepository;

    @Autowired
    ShelfItemRepository shelfItemRepository;

    public List<ShelfDTO> getAllShelves() {
        return shelfRepository.findAll().stream().map(ShelfDTO::toDto).toList();
    }

    public List<ShelfItemDTO> getShelfItemsById(Integer idShelf) throws NoSuchObjectException {
        if (shelfRepository.findById(idShelf).isPresent())
            return shelfRepository.findById(idShelf).get().getShelfItems().stream().map(ShelfItemDTO::toDto).toList();
        else
            throw new NoSuchObjectException("Shelf with id " + idShelf + " does not exist.");
    }

    public ShelfDTO createShelf(ShelfDTO shelf) {
        Shelf createShelf = new Shelf(shelf.getDimensionX(), shelf.getDimensionY());
        return ShelfDTO.toDto(shelfRepository.save(createShelf));
    }


    public void updateShelf(UpdateShelfDTO shelf) throws NoSuchObjectException {
        if(!shelfRepository.existsById(shelf.getIdShelf()))
            throw new NoSuchObjectException("Shelf with id " + shelf.getIdShelf() + " does not exist.");
        Shelf updateShelf = shelfRepository.findById(shelf.getIdShelf()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(shelf.getDimensionX()).ifPresent(updateShelf::setDimensionX);
        Optional.ofNullable(shelf.getDimensionY()).ifPresent(updateShelf::setDimensionY);
        shelfRepository.save(updateShelf);
    }

    public void addShelfItem(Integer shelfId, Integer idShelfItem) throws NoSuchObjectException {
        if(!shelfRepository.existsById(shelfId))
            throw new NoSuchObjectException("Shelf with id " + shelfId + " does not exist.");
        if(!shelfItemRepository.existsById(idShelfItem))
            throw new IllegalArgumentException("Shelf item with id " + idShelfItem + " does not exist.");
        ShelfItem shelfItem = shelfItemRepository.findById(idShelfItem).orElseThrow(NullPointerException::new);
        Shelf shelf = shelfRepository.findById(shelfId).orElseThrow(NullPointerException::new);
        Set<ShelfItem> shelfItems = shelf.getShelfItems();
        if(shelfItems == null) {
            shelfItems = new HashSet<>();
        }
        shelfItems.add(shelfItem);
        shelf.setShelfItems(shelfItems);
        shelfRepository.save(shelf);
    }

    public void removeShelfItem(Integer shelfId, Integer itemId) throws NoSuchObjectException {
        if(!shelfRepository.existsById(shelfId))
            throw new NoSuchObjectException("Shelf with id " + shelfId + " does not exist.");
        if(!shelfItemRepository.existsById(itemId))
            throw new IllegalArgumentException("Shelf item with id " + itemId + " does not exist.");
        ShelfItem shelfItem = shelfItemRepository.findById(itemId).orElseThrow(NullPointerException::new);
        Shelf shelf = shelfRepository.findById(shelfId).orElseThrow(NullPointerException::new);
        Set<ShelfItem> shelfItems = shelf.getShelfItems();
        if(shelfItems != null) {
            shelfItems.remove(shelfItem);
            shelf.setShelfItems(shelfItems);
            shelfRepository.save(shelf);
        }
    }

    public void deleteShelf(Integer id) throws NoSuchObjectException {
        if(!shelfRepository.existsById(id))
            throw new NoSuchObjectException("Shelf with id " + id + " does not exist.");
        shelfRepository.deleteById(id);
    }

}
