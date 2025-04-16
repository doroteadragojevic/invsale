package fer.hr.invsale.DTO.order;

import fer.hr.invsale.DAO.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateOrderDTO {
    @NotNull
    private Integer idOrder;
    @Nullable
    private PaymentMethod paymentMethod;
    @Nullable
    private String shippingAddress;

}
