package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents product category.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /** Category name. Name is also a table key.*/
    @Id
    @NonNull
    private String name;
    /** Category description. It does not have to be present. */
    @Nullable
    private String description;

}
