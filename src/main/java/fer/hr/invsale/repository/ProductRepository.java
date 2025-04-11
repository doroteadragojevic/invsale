package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
