package fer.hr.invsale.DTO.shelfItem;

import fer.hr.invsale.DAO.ShelfItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfItemDTO {
    private Integer idShelfItem;
    private Double dimensionX;
    private Double dimensionY;
    private Integer productId;
    private String productName;
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
