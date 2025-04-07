package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProduct;
    @ManyToOne
    @Nullable
    private Manufacturer manufacturer;
    @NonNull
    private String name;
    @Nullable
    private Set<String> ingredients;
    @NonNull
    private String description;
    @Lob
    private byte[] imageData;
    /** Product threshold on stack when seller gets notifications about restocking the product. */
    private Integer reorderNotificationThreshold;
    @ManyToMany
    Set<Category> categories;
    @ManyToMany
    Set<Unit> quantityUnits;

}
