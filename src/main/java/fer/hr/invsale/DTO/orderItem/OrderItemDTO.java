package fer.hr.invsale.DTO.orderItem;

import fer.hr.invsale.DAO.OrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer idOrderItem;
    @NonNull
    private Integer productId;
    private String productName;
    @NonNull
    private Integer unitId;
    private String unitName;
    @NonNull
    private Integer quantity;
    @NotNull
    private Integer orderId;

    public static OrderItemDTO toDto(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getIdOrderItem(),
                orderItem.getProduct().getIdProduct(),
                orderItem.getProduct().getName(),
                orderItem.getUnit().getIdUnit(),
                orderItem.getUnit().getName(),
                orderItem.getQuantity(),
                orderItem.getOrder().getIdOrder());
    }

}
