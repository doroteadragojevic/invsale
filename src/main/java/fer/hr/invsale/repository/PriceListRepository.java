package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.PriceList;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Integer> {
    List<PriceList> findAllByProduct_IdProduct(Integer idProduct);

    void deleteAllByProduct_IdProduct(Integer id);

    void deleteAllByUnit_IdUnit(Integer id);

    Optional<PriceList> findByProductAndUnit(Product product, Unit unit);
}
