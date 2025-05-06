package fer.hr.invsale.DTO.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneProductsDTO {
    private String zone; // "najblize", "sredina", "udaljeno"
    private List<String> productNames;
}
