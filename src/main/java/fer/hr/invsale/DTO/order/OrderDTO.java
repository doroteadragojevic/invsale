package fer.hr.invsale.DTO.order;

import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DAO.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer idOrder;
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
                order.getTotalPrice(),
                order.getOrderStatus().getName(),
                order.getOrderTimestamp(),
                order.getPaymentMethod(),
                order.getShippingAddress()
        );
    }
}
