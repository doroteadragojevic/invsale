package fer.hr.invsale.DTO.unit;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateUnitDTO {
    @NonNull
    private Integer idUnit;
    @Nullable
    private String name;
    @Nullable
    private String description;
}
