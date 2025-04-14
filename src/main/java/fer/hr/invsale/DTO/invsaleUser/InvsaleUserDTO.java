package fer.hr.invsale.DTO.invsaleUser;

import fer.hr.invsale.DAO.InvsaleUser;
import lombok.*;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class InvsaleUserDTO {
    @NonNull
    private String email;
    @NonNull
    private String role;
    @Nullable
    private String phoneNumber;

    public static InvsaleUserDTO toDto(InvsaleUser user){
        return new InvsaleUserDTO(user.getEmail(), user.getRole().getName(), user.getPhoneNumber());
    }
}
