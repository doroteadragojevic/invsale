package fer.hr.invsale.DAO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCoupon;
    /** Coupon code. It can be a combination of letters and numbers. */
    @NonNull
    private String code;
    /** Coupon name. It should explain the occasion i.e. "new user", "black friday" etc. */
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
    private Map<Integer, Integer> usagesByUser;
    /** Discount amount. */
    @NonNull
    private Double discount;
}
