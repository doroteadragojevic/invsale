package fer.hr.invsale.DTO;

import fer.hr.invsale.DAO.Coupon;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @NonNull
    private String code;
    private String name;
    @Nullable
    private Timestamp dateTimeFrom;
    @Nullable
    private Timestamp dateTimeTo;
    @NotNull
    private Integer usageLimit;
    @NonNull
    private Double discount;

    public static CouponDTO toDto(Coupon coupon) {
        return new CouponDTO(
                coupon.getCode(),
                coupon.getName(),
                coupon.getDateTimeFrom(),
                coupon.getDateTimeTo(),
                coupon.getUsageLimit(),
                coupon.getDiscount()
        );
    }

}
