package fer.hr.invsale.DTO.role;

import fer.hr.invsale.DAO.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NonNull
    private String name;

    public static RoleDTO toDto(Role role) {
        return new RoleDTO(role.getName());
    }
}
