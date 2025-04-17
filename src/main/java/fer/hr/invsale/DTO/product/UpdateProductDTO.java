package fer.hr.invsale.DTO.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Set;

@Data
@NoArgsConstructor
public class UpdateProductDTO {

    @NonNull
    private Integer idProduct;
    private Integer idManufacturer;
    private String name;
    private Set<String> ingredients;
    private String description;
    private byte[] imageData;
    private Integer reorderNotificationThreshold;
    private Integer quantityOnStock;

}
