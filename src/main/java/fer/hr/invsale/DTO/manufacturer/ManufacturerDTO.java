package fer.hr.invsale.DTO.manufacturer;

import fer.hr.invsale.DAO.Manufacturer;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ManufacturerDTO {
    private Integer idManufacturer;
    @NonNull
    private String name;

    public static ManufacturerDTO toDto(Manufacturer manufacturer) {
        return new ManufacturerDTO(manufacturer.getIdManufacturer(), manufacturer.getName());
    }
}
