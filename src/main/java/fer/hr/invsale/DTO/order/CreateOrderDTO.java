package fer.hr.invsale.DTO.order;

import fer.hr.invsale.DAO.PaymentMethod;
import lombok.*;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    @NonNull
    private String email;
    @Nullable
    private PaymentMethod paymentMethod;
    @Nullable
    private String shippingAddress;
}
