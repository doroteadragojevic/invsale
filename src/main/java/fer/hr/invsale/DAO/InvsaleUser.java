package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InvsaleUser {

    /** User email can be provided by user register or by OAuth. */
    @Id
    @NonNull
    private String email;
    @ManyToOne
    private Role role;
    /** Hashed password is stored only for user that do not use OAuth registration. */
    @Nullable
    private String hashedPassword;
    /** List that contains liked product ids. */
    @Nullable
    @ManyToMany
    private List<Product> likedProducts;

}
