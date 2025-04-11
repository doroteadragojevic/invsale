package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * This class represents the space certain product will take depending on product unit.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShelfItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idShelfItem;
    private Double dimensionX;
    private Double dimensionY;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Unit unit;
}
