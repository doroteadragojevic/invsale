package fer.hr.invsale.DTO.shelf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateShelfDTO {
    @NonNull
    private Integer idShelf;
    @Nullable
    private Double dimensionX;
    @Nullable
    private Double dimensionY;
}
