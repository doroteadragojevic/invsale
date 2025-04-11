package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.coupon.CouponDTO;
import fer.hr.invsale.DTO.coupon.UpdateCouponDTO;
import fer.hr.invsale.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    CouponService couponService;

    @GetMapping("/")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CouponDTO> getCouponByCode(@PathVariable String code) {
        return couponService.getCouponByCode(code)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO coupon) {
        try{
            return new ResponseEntity<>(couponService.createCoupon(coupon), HttpStatus.CREATED);
        }catch(KeyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateCoupon(@RequestBody UpdateCouponDTO coupon) {
        try{
            couponService.updateCoupon(coupon);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String code) {
        try{
            couponService.deleteCoupon(code);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
