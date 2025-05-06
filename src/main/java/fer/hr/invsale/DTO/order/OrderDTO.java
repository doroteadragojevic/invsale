package fer.hr.invsale.DTO.order;

import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.PaymentMethod;
import lombok.*;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OrderDTO {
    private Integer idOrder;
    private String email;
    @NonNull
    private Double totalPrice;
    @NonNull
    private String orderStatusName;
    @NonNull
    private Timestamp orderTimestamp;
    private PaymentMethod paymentMethod;
    @Nullable
    private String shippingAddress;

    public static OrderDTO toDto(Order order) {
        return new OrderDTO(
                order.getIdOrder(),
                order.getInvsaleUser().getEmail(),
                order.getTotalPrice(),
                order.getOrderStatus().getName(),
                order.getOrderTimestamp(),
                order.getPaymentMethod(),
                order.getShippingAddress()
        );
    }
}
