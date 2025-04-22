package fer.hr.invsale.DTO.shelf;

import fer.hr.invsale.DAO.Shelf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfDTO {

    private Integer idShelf;
    @NonNull
    private Double dimensionX;
    @NonNull
    private Double dimensionY;

    public static ShelfDTO toDto(Shelf shelf) {
        return new ShelfDTO(
                shelf.getIdShelf(),
                shelf.getDimensionX(),
                shelf.getDimensionY()
        );
    }


}
