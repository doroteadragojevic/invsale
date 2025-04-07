package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
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
    private Product product;
    @ManyToOne
    private Unit unit;
    @NonNull
    private Double price;
    /** Price activation date and time. */
    @NonNull
    private Timestamp dateTimeFrom;
    /** Price expiry date and time. */
    @NonNull
    private Timestamp dateTimeTo;
    @Nullable
    private Double discount;

}
