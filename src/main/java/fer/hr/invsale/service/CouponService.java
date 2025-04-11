package fer.hr.invsale.service;

import fer.hr.invsale.DTO.CouponDTO;
import fer.hr.invsale.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
