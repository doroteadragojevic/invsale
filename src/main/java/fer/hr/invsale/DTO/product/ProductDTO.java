package fer.hr.invsale.DTO.product;

import fer.hr.invsale.DAO.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer idProduct;
    private Integer idManufacturer;
    private String manufacturerName;
    @NonNull
    private String name;
    @Nullable
    private Set<String> ingredients;
    @NonNull
    private String description;
    private byte[] imageData;
    private Integer reorderNotificationThreshold;
    @NonNull
    private Integer quantityOnStock;

    public static ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getIdProduct(),
                product.getManufacturer() == null ? null : product.getManufacturer().getIdManufacturer(),
                product.getManufacturer() == null ? null : product.getManufacturer().getName(),
                product.getName(),
                product.getIngredients(),
                product.getDescription(),
                product.getImageData(),
                product.getReorderNotificationThreshold(),
                product.getQuantityOnStock()
        );
    }
}
