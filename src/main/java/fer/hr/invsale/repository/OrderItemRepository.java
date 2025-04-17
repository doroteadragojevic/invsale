package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.OrderItem;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrder_IdOrder(Integer orderId);

    Optional<OrderItem> findByProductAndUnitAndOrder(Product product, Unit unit, Order order);

    void deleteAllByOrder_IdOrder(Integer orderId);

    void deleteAllByProduct_IdProduct(Integer id);
}
