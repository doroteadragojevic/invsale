package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
