package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
