package fer.hr.invsale.service;

import fer.hr.invsale.DAO.PriceList;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Unit;
import fer.hr.invsale.DTO.priceList.PriceListDTO;
import fer.hr.invsale.DTO.priceList.UpdatePriceListDTO;
import fer.hr.invsale.repository.PriceListRepository;
import fer.hr.invsale.repository.ProductRepository;
import fer.hr.invsale.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PriceListService {

    @Autowired
    PriceListRepository priceListRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UnitRepository unitRepository;

    public List<PriceListDTO> getActivePriceLists() {
        return priceListRepository.findAll()
                .stream()
                .filter(
                        this::isActive)
                .map(PriceListDTO::toDto)
                .toList();
    }

    public PriceListDTO createPriceList(PriceListDTO priceList) throws InstanceAlreadyExistsException, NoSuchObjectException {
        validateExistence(priceList);
        if (!uniqueConditionMet(priceList))
            throw new InstanceAlreadyExistsException("Active price list with given product and unit already exists. Please delete active price list or update existent price list.");
        return PriceListDTO.toDto(createFromDto(priceList));
    }

    private PriceList createFromDto(PriceListDTO priceList) {
        Product product = productRepository.findById(priceList.getIdProduct()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(priceList.getUnitId()).orElseThrow(NullPointerException::new);
        if (!product.getQuantityUnits().contains(unit))
            throw new IllegalArgumentException("This product does not contain this unit.");
        PriceList createPriceList = new PriceList();
        createPriceList.setProduct(product);
        createPriceList.setUnit(unit);
        createPriceList.setPriceWithoutDiscount(priceList.getPrice());
        Optional.ofNullable(priceList.getDiscount()).ifPresent(createPriceList::setDiscount);
        createPriceList.setDateTimeFrom(priceList.getDateTimeFrom());
        createPriceList.setDateTimeTo(priceList.getDateTimeTo());
        return priceListRepository.save(createPriceList);
    }

    private void validateExistence(PriceListDTO priceList) throws NoSuchObjectException {
        if ((priceList.getIdPriceList() != null &&!priceListRepository.existsById(priceList.getIdPriceList()))
            || !productRepository.existsById(priceList.getIdProduct())
                || !unitRepository.existsById(priceList.getUnitId()))
            throw new NoSuchObjectException("Product, price list or unit does not exist.");
    }

    private void validateExistenceUpdate(UpdatePriceListDTO priceList) throws NoSuchObjectException {
        if (!priceListRepository.existsById(priceList.getIdPriceList())
                || (priceList.getIdProduct() != null && !productRepository.existsById(priceList.getIdProduct()))
                || (priceList.getUnitId() != null && !unitRepository.existsById(priceList.getUnitId())))
            throw new NoSuchObjectException("Product, price list or unit does not exist.");
    }

    private boolean uniqueConditionMet(PriceListDTO priceList) {
        Product product = productRepository.findById(priceList.getIdProduct()).orElseThrow(NullPointerException::new);
        Unit unit = unitRepository.findById(priceList.getUnitId()).orElseThrow(NullPointerException::new);
        return priceListRepository.findByProductAndUnit(product, unit).isEmpty();
    }

    private boolean isActive(PriceList priceList) {
        return priceList.getDateTimeFrom().before(Timestamp.valueOf(LocalDateTime.now()))
                && priceList.getDateTimeTo().after(Timestamp.valueOf(LocalDateTime.now()));
    }

    public void updatePriceList(UpdatePriceListDTO priceList) throws NoSuchObjectException, InstanceAlreadyExistsException {
        validateExistenceUpdate(priceList);
        if ((priceList.getIdProduct()!= null || priceList.getUnitId()!= null) && !uniqueConditionMetUpdate(priceList))
            throw new InstanceAlreadyExistsException("Active price list with given product and unit already exists. Please delete active price list or update existent price list.");
        updateFromDto(priceList);
    }

    private void updateFromDto(UpdatePriceListDTO priceList) {
        PriceList updatePriceList = priceListRepository.findById(priceList.getIdPriceList()).orElseThrow(NullPointerException::new);
        if(priceList.getIdProduct() != null) {
            Product product = productRepository.findById(priceList.getIdProduct()).orElseThrow(NullPointerException::new);
            updatePriceList.setProduct(product);
        }
        if(priceList.getUnitId() != null) {
            Unit unit = unitRepository.findById(priceList.getUnitId()).orElseThrow(NullPointerException::new);
            updatePriceList.setUnit(unit);
        }
        if(priceList.getIdProduct() != null || priceList.getUnitId() != null) {
            Product product = productRepository.findById(updatePriceList.getProduct().getIdProduct()).orElseThrow(NullPointerException::new);
            Unit unit = unitRepository.findById(updatePriceList.getUnit().getIdUnit()).orElseThrow(NullPointerException::new);
            if (!product.getQuantityUnits().contains(unit))
                throw new IllegalArgumentException("This product does not contain this unit.");
        }
        Optional.ofNullable(priceList.getPrice()).ifPresent(updatePriceList::setPriceWithoutDiscount);
        Optional.of(priceList.getDateTimeFrom()).ifPresent(updatePriceList::setDateTimeFrom);
        Optional.of(priceList.getDateTimeTo()).ifPresent(updatePriceList::setDateTimeTo);
        Optional.ofNullable(priceList.getDiscount()).ifPresent(updatePriceList::setDiscount);
        priceListRepository.save(updatePriceList);
    }

    private boolean uniqueConditionMetUpdate(UpdatePriceListDTO priceList) {
        PriceList pl = priceListRepository.findById(priceList.getIdPriceList()).orElseThrow(NullPointerException::new);
        if(!Objects.equals(pl.getProduct().getIdProduct(), priceList.getIdProduct())
        || !Objects.equals(pl.getUnit().getIdUnit(), priceList.getUnitId())) {
            Product product = productRepository.findById(priceList.getIdProduct()).orElseThrow(NullPointerException::new);
            Unit unit = unitRepository.findById(priceList.getUnitId()).orElseThrow(NullPointerException::new);
            return priceListRepository.findByProductAndUnit(product, unit).isEmpty();
        }
        return true;
    }

    public List<PriceListDTO> getActivePriceListsForProduct(Integer idProduct) throws NoSuchObjectException {
        if(!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        return priceListRepository.findAllByProduct_IdProduct(idProduct).stream().filter(this::isActive).map(PriceListDTO::toDto).toList();
    }

    public void deletePriceList(Integer id) throws NoSuchObjectException {
        if(!priceListRepository.existsById(id))
            throw new NoSuchObjectException("Price list with id " + id + " does not exist.");
        priceListRepository.deleteById(id);
    }

    public PriceListDTO getActivePriceList(Integer idProduct, Integer idUnit) throws NoSuchObjectException {
        if(!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        Product product = productRepository.findById(idProduct).get();
        if(!unitRepository.existsById(idUnit))
            throw new NoSuchObjectException("Unit with id " + idProduct + " does not exist.");
        Unit unit = unitRepository.findById(idUnit).get();
        return PriceListDTO.toDto(priceListRepository.findAllByProductAndUnit(product, unit).stream().filter(this::isActive).findFirst().get());
    }

    public PriceListDTO getPriceListByDate(Integer idProduct, Integer idUnit, Timestamp date) throws NoSuchObjectException {
        if(!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        Product product = productRepository.findById(idProduct).get();
        if(!unitRepository.existsById(idUnit))
            throw new NoSuchObjectException("Unit with id " + idProduct + " does not exist.");
        Unit unit = unitRepository.findById(idUnit).get();
        return PriceListDTO.toDto(priceListRepository.findAllByProductAndUnit(product, unit)
                .stream().filter(pl -> pl.getDateTimeFrom().before(date)
                && pl.getDateTimeTo().after(date))
                .findFirst().get()
        );
    }
}
