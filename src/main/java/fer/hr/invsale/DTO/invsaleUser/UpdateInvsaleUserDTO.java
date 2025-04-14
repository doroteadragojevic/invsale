package fer.hr.invsale.DTO.invsaleUser;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvsaleUserDTO {
    @NonNull
    private String email;
    /** Hashed password is stored only for user that do not use OAuth registration. */
    @Nullable
    private String password;
    @Nullable
    private String phoneNumber;
}
