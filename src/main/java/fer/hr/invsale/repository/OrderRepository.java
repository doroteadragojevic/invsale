package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order>  findAllByInvsaleUser_Email(String email);
}
