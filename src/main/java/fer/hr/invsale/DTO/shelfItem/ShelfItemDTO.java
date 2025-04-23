package fer.hr.invsale.DTO.shelfItem;

import fer.hr.invsale.DAO.ShelfItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfItemDTO {
    @NonNull
    private Integer idShelfItem;
    @NonNull
    private Double dimensionX;
    @NonNull
    private Double dimensionY;
    @NonNull
    private Integer productId;
    private String productName;
    @NonNull
    private Integer unitId;
    private String unitName;

    public static ShelfItemDTO toDto(ShelfItem shelfItem) {
        return new ShelfItemDTO(
                shelfItem.getIdShelfItem(),
                shelfItem.getDimensionX(),
                shelfItem.getDimensionY(),
                shelfItem.getProduct().getIdProduct(),
                shelfItem.getProduct().getName(),
                shelfItem.getUnit().getIdUnit(),
                shelfItem.getUnit().getName()
        );
    }
}
