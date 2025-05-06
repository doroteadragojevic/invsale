package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByProduct_IdProduct(Integer productId);

    Optional<Reservation> findByProductAndUser(Product product, InvsaleUser user);
}
