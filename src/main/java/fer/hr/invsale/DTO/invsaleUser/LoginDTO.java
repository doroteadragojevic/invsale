package fer.hr.invsale.DTO.invsaleUser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LoginDTO {

    @NonNull
    String email;
    @NonNull
    String password;

}
