package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Coupon;
import fer.hr.invsale.DTO.coupon.CouponDTO;
import fer.hr.invsale.DTO.coupon.UpdateCouponDTO;
import fer.hr.invsale.repository.CouponRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    CouponRepository couponRepository;

    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream().map(CouponDTO::toDto).toList();
    }

    public Optional<CouponDTO> getCouponByCode(String code) {
        return couponRepository.findById(code).map(CouponDTO::toDto);
    }

    public CouponDTO createCoupon(CouponDTO coupon) {
        if(couponRepository.existsById(coupon.getCode()))
            throw new KeyAlreadyExistsException("Coupon code " + coupon.getName() + " already exists.");
        return CouponDTO.toDto(
                couponRepository.save(createCouponFromDto(coupon)));
    }

    public void updateCoupon(UpdateCouponDTO coupon) throws NoSuchObjectException {
        if(!couponRepository.existsById(coupon.getCode()))
            throw new NoSuchObjectException("Coupon with code \"" + coupon.getCode() + "\" does not exist.");

        couponRepository.save(updateFromDto(coupon));

    }

    private Coupon updateFromDto(UpdateCouponDTO coupon) {
        Coupon newCoupon = couponRepository
                .findById(coupon.getCode())
                .orElseThrow(() -> new NullPointerException("Coupon does not exist."));
        Optional.ofNullable(coupon.getName()).ifPresent(newCoupon::setName);
        Optional.ofNullable(coupon.getDateTimeFrom()).ifPresent(newCoupon::setDateTimeFrom);
        Optional.ofNullable(coupon.getDateTimeTo()).ifPresent(newCoupon::setDateTimeTo);
        Optional.ofNullable(coupon.getUsageLimit()).ifPresent(newCoupon::setUsageLimit);
        Optional.ofNullable(coupon.getDiscount()).ifPresent(newCoupon::setDiscount);
        return newCoupon;
    }

    private Coupon createCouponFromDto(CouponDTO coupon) {
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(coupon.getCode());
        newCoupon.setName(coupon.getName());
        Optional.ofNullable(coupon.getDateTimeFrom()).ifPresent(newCoupon::setDateTimeFrom);
        Optional.ofNullable(coupon.getDateTimeTo()).ifPresent(newCoupon::setDateTimeTo);
        newCoupon.setUsageLimit(coupon.getUsageLimit());
        newCoupon.setDiscount(coupon.getDiscount());
        return newCoupon;
    }

    public void deleteCoupon(String code) throws NoSuchObjectException {
        if(!couponRepository.existsById(code))
            throw new NoSuchObjectException("Coupon with code \"" + code + "\" does not exist.");

        couponRepository.deleteById(code);
    }

    public void couponApplied(@NonNull String email, String code) {
        Coupon coupon = couponRepository.findById(code).get();
        if(coupon.getUsagesByUser() == null)
            coupon.setUsagesByUser(new HashMap<>());
        if(coupon.getUsagesByUser().containsKey(email) && coupon.getUsagesByUser().get(email) >= coupon.getUsageLimit())
            throw new IllegalStateException();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if(coupon.getDateTimeFrom().after(now) || coupon.getDateTimeTo().before(now))
            throw new IllegalStateException();
        Map<String, Integer> userUsages = coupon.getUsagesByUser();
        if(!coupon.getUsagesByUser().containsKey(email)){
            userUsages.put(email, 1);
        } else {
            userUsages.compute(email, (k, usages) -> usages + 1);
        }
        coupon.setUsagesByUser(userUsages);
        couponRepository.save(coupon);
    }

    public void couponRemoved(@NonNull String email, String code) {
        Coupon coupon = couponRepository.findById(code).get();
        Map<String, Integer> userUsages = coupon.getUsagesByUser();
        if(coupon.getUsagesByUser().containsKey(email)){
            userUsages.compute(email, (k, usages) -> usages - 1);
            coupon.setUsagesByUser(userUsages);
            couponRepository.save(coupon);
        }

    }
}
