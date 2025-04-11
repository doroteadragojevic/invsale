package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, String> {
}
