package fer.hr.invsale.DAO;

import fer.hr.invsale.util.MapToJsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Coupon is applied to whole order and is given to user i.e. by email.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    /** Coupon code. It can be a combination of letters and numbers. */
    @NotNull
    @Id
    private String code;
    /** Coupon name. It should explain the occasion i.e. "new user", "black friday" etc. */
    @NotNull
    private String name;
    /**
     * Coupon activation date and time.
     * Coupon does not need to have activation date. For example coupon "new user" is applied when there is a new buyer,
     * and there is no need to have activation or expiration date.
     */
    @Nullable
    private Timestamp dateTimeFrom;
    /**
     * Coupon expiration date.
     */
    @Nullable
    private Timestamp dateTimeTo;
    /**
     * Limit of times a single user can use the coupon.
     */
    @NotNull
    private Integer usageLimit;
    /**
     * InvsaleUsers ID is key of this map, value is number of usages.
     * This map represents coupon usages by each user.
     */
    @Convert(converter = MapToJsonConverter.class)
    private Map<Integer, Integer> usagesByUser;
    /** Discount amount. */
    @NotNull
    private Double discount;
}
