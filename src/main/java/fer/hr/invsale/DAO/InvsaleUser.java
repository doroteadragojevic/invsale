package fer.hr.invsale.DAO;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
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
    @Nullable
    private String phoneNumber;

}
