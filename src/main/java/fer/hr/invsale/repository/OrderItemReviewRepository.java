package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.OrderItemReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemReviewRepository extends JpaRepository<OrderItemReview, Integer> {
    void deleteAllByOrderItem_IdOrderItem(Integer orderItemId);
}
