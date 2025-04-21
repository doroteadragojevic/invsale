package fer.hr.invsale.DTO.priceList;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class UpdatePriceListDTO {

    private Integer idPriceList;
    private Integer idProduct;
    private Integer unitId;
    private Double price;
    private Timestamp dateTimeFrom;
    private Timestamp dateTimeTo;
    private Double discount;

}
