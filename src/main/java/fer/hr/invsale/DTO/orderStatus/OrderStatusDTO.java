package fer.hr.invsale.DTO.orderStatus;

import fer.hr.invsale.DAO.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    @NonNull
    private String name;
    @Nullable
    private String description;

    public static OrderStatusDTO toDto(OrderStatus orderStatus) {
        return new OrderStatusDTO(orderStatus.getName(), orderStatus.getDescription());
    }
}
