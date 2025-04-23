package fer.hr.invsale.DTO.stock;

import fer.hr.invsale.DAO.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private Integer idStock;
    @NonNull
    private String name;

    public static StockDTO toDto(Stock stock) {
        return new StockDTO(
                stock.getIdStock(),
                stock.getName()
        );
    }

}
