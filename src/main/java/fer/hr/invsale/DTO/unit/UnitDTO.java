package fer.hr.invsale.DTO.unit;

import fer.hr.invsale.DAO.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDTO {
    private Integer idUnit;
    @NonNull
    private String name;
    @Nullable
    private String description;

    public static UnitDTO toDto(Unit unit) {
        return new UnitDTO(unit.getIdUnit(), unit.getName(), unit.getDescription());
    }
}
