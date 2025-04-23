package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.ShelfItem;
import fer.hr.invsale.DAO.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfItemRepository extends JpaRepository<ShelfItem, Integer> {
    List<ShelfItem> findByProductAndUnit(Product product, Unit unit);
}
