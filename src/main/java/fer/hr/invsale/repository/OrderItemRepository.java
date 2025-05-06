package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.OrderItem;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrder_IdOrder(Integer orderId);

    Optional<OrderItem> findByProductAndUnitAndOrder(Product product, Unit unit, Order order);

    void deleteAllByOrder_IdOrder(Integer orderId);

    void deleteAllByProduct_IdProduct(Integer id);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.idProduct = :productId AND oi.order.orderTimestamp BETWEEN :startDate AND :endDate")
    int sumQuantityByProductIdAndDateRange(@Param("productId") Integer productId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);



    @Query("SELECT oi.product.name, SUM(oi.quantity) as total " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderTimestamp >= :start " +
            "GROUP BY oi.product.idProduct, oi.product.name " +
            "ORDER BY total DESC")
    List<Object[]> findProductPopularityLast3Months(@Param("start") LocalDateTime start);


}
