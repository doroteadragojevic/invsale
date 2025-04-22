package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReviewRepository extends JpaRepository<OrderReview, Integer> {
    void deleteAllByOrder_IdOrder(Integer idOrder);
}
