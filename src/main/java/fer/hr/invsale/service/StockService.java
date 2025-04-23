package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Shelf;
import fer.hr.invsale.DAO.Stock;
import fer.hr.invsale.DTO.shelf.ShelfDTO;
import fer.hr.invsale.DTO.stock.StockDTO;
import fer.hr.invsale.repository.ShelfRepository;
import fer.hr.invsale.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StockService {

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ShelfRepository shelfRepository;

    @Autowired
    ShelfService shelfService;

    public List<ShelfDTO> getShelvesInStock(Integer id) throws NoSuchObjectException {
        if(stockRepository.findById(id).isPresent())
            return stockRepository.findById(id).get().getShelves().stream().map(ShelfDTO::toDto).toList();
        throw new NoSuchObjectException("Stock with id " + id + " does not exist.");
    }

    public StockDTO createStock(StockDTO stock) {
        Stock createStock = new Stock();
        createStock.setName(stock.getName());
        return StockDTO.toDto(stockRepository.save(createStock));
    }

    public void updateStock(StockDTO stock) throws NoSuchObjectException {
        if(stockRepository.findById(stock.getIdStock()).isPresent()){
            Stock updateStock = stockRepository.findById(stock.getIdStock()).get();
            updateStock.setName(stock.getName());
            stockRepository.save(updateStock);
        } else {
            throw new NoSuchObjectException("Stock with id " + stock.getIdStock() + " does not exist.");
        }
    }

    public void addShelf(Integer id, Integer shelfId) throws NoSuchObjectException {
        if(stockRepository.findById(id).isPresent()){
            if(shelfRepository.findById(shelfId).isPresent()){
                Shelf shelf = shelfRepository.findById(shelfId).get();
                Stock stock = stockRepository.findById(id).get();
                Set<Shelf> shelves = stock.getShelves();
                if(shelves == null)
                    shelves = new HashSet<>();
                shelves.add(shelf);
                stockRepository.save(stock);
            } else {
                throw new IllegalArgumentException("Shelf with id " + shelfId + " does not exist.");
            }
        } else {
            throw new NoSuchObjectException("Stock with id " + id + " does not exist.");
        }
    }

    public void removeShelf(Integer id, Integer shelfId) throws NoSuchObjectException {
        if(stockRepository.findById(id).isPresent()){
            if(shelfRepository.findById(shelfId).isPresent()){
                Shelf shelf = shelfRepository.findById(shelfId).get();
                Stock stock = stockRepository.findById(id).get();
                Set<Shelf> shelves = stock.getShelves();
                if(shelves != null){
                    shelves.remove(shelf);
                    stockRepository.save(stock);
                }
            } else {
                throw new IllegalArgumentException("Shelf with id " + shelfId + " does not exist.");
            }
        } else {
            throw new NoSuchObjectException("Stock with id " + id + " does not exist.");
        }
    }

    public void deleteStock(Integer id) throws NoSuchObjectException {
        if(!stockRepository.existsById(id))
            throw new NoSuchObjectException("Stock with id " + id + " does not exist.");
        for(Integer idShelf : stockRepository.findById(id).get().getShelves().stream().map(Shelf::getIdShelf).toList())
            shelfService.deleteShelf(idShelf);
        stockRepository.deleteById(id);
    }
}
