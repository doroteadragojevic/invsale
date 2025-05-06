package fer.hr.invsale.DTO.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStatsDTO {
    private Integer productId;
    private String productName;
    private int predictedNextMonthDemand;
    private int currentStock;
    private boolean reorderNeeded;
}
