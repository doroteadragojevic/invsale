package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.ShelfItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfItemRepository extends JpaRepository<ShelfItem, Integer> {
}
