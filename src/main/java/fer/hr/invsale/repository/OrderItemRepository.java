package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
