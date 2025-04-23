package fer.hr.invsale.DTO.shelfItem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateShelfItemDTO {

    @NonNull
    private Integer idShelfItem;
    @Nullable
    private Double dimensionX;
    @Nullable
    private Double dimensionY;

}
