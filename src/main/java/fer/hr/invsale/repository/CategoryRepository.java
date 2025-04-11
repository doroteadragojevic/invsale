package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
