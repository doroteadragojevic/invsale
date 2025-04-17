package fer.hr.invsale.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;


/**
 * This class represents quantity unit for product. I.e. 1pc, crate etc.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUnit;
    @NonNull
    private String name;
    @Nullable
    private String description;

    public Unit(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
