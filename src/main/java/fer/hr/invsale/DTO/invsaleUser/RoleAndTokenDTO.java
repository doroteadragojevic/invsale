package fer.hr.invsale.DTO.invsaleUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAndTokenDTO {
    @NonNull
    Boolean isAdmin;
    @NonNull
    String token;
}
