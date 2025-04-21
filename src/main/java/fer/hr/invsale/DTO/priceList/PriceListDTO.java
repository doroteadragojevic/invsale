package fer.hr.invsale.DTO.priceList;

import fer.hr.invsale.DAO.PriceList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDTO {

    private Integer idPriceList;
    @NonNull
    private Integer idProduct;
    private String productName;
    @NonNull
    private Integer unitId;
    private String unitName;
    @NonNull
    private Double price;
    @NonNull
    private Timestamp dateTimeFrom;
    @NonNull
    private Timestamp dateTimeTo;
    @Nullable
    private Double discount;

    public static PriceListDTO toDto(PriceList priceList) {
        return new PriceListDTO(
                priceList.getIdPriceList(),
                priceList.getProduct().getIdProduct(),
                priceList.getProduct().getName(),
                priceList.getUnit().getIdUnit(),
                priceList.getUnit().getName(),
                priceList.getPriceWithoutDiscount(),
                priceList.getDateTimeFrom(),
                priceList.getDateTimeTo(),
                priceList.getDiscount()
        );
    }
}
