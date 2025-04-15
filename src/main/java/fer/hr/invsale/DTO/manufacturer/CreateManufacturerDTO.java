package fer.hr.invsale.DTO.manufacturer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateManufacturerDTO {
    @NonNull
    private String name;
}
