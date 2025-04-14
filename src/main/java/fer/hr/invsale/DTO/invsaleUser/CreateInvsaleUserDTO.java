package fer.hr.invsale.DTO.invsaleUser;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateInvsaleUserDTO {
    @NonNull
    private String email;
    @NonNull
    private String role;
    /** Hashed password is stored only for user that do not use OAuth registration. */
    @Nullable
    private String password;
    @Nullable
    private String phoneNumber;

}
