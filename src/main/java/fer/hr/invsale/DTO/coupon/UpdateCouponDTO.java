package fer.hr.invsale.DTO.coupon;

import lombok.*;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCouponDTO {
    @NonNull
    private String code;
    @Nullable
    private String name;
    @Nullable
    private Timestamp dateTimeFrom;
    @Nullable
    private Timestamp dateTimeTo;
    @Nullable
    private Integer usageLimit;
    @Nullable
    private Double discount;
}
