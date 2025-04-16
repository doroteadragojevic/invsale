package fer.hr.invsale.DTO.order;

import fer.hr.invsale.DAO.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateOrderDTO {
    @NonNull
    private String email;
    @Nullable
    private PaymentMethod paymentMethod;
    @Nullable
    private String shippingAddress;
}
