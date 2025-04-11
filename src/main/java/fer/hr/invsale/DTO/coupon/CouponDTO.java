package fer.hr.invsale.DTO.coupon;

import fer.hr.invsale.DAO.Coupon;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {

    @NonNull
    private String code;
    @NonNull
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
