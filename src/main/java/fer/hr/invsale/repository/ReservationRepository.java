package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Reservation;
import fer.hr.invsale.DAO.Unit;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByProduct_IdProductAndUnit_IdUnit(Integer productId, Integer unidId);

    Optional<Reservation> findByProductAndUserAndUnit(Product product, InvsaleUser user, Unit unit);

    void deleteAllByOrder_IdOrder(Integer idOrder);
}
