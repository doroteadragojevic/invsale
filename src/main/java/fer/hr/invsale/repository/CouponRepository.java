package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
}
