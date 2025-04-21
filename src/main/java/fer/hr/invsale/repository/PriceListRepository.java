package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.PriceList;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceListRepository extends JpaRepository<PriceList, Integer> {
    List<PriceList> findAllByProductAndUnit(Product product, Unit unit);

    List<PriceList> findAllByProduct_IdProduct(Integer idProduct);

    void deleteAllByProduct_IdProduct(Integer id);

    void deleteAllByUnit_IdUnit(Integer id);
}
