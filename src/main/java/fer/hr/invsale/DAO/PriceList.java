package fer.hr.invsale.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

/**
 * This class represents a price list.
 * One price is defined by unit of one product.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPriceList;
    @ManyToOne
    @NonNull
    private Product product;
    @ManyToOne
    @NonNull
    private Unit unit;
    @NonNull
    private Double priceWithoutDiscount;
    /** Price activation date and time. */
    @NonNull
    private Timestamp dateTimeFrom;
    /** Price expiry date and time. */
    @NonNull
    private Timestamp dateTimeTo;
    @Nullable
    private Double discount;

}
