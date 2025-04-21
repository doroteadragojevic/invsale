package fer.hr.invsale.DTO.review;

import fer.hr.invsale.DAO.OrderItemReview;
import fer.hr.invsale.DAO.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemReviewDTO {
    private Integer idReview;
    @NonNull
    private Rating rating;
    @Nullable
    private String comment;
    @NonNull
    private Date reviewDate;
    @NonNull
    private String invsaleUser;
    @NonNull
    private Integer orderItemId;

    public static OrderItemReviewDTO toDto(OrderItemReview orderItemReview) {
        return new OrderItemReviewDTO(
                orderItemReview.getIdReview(),
                orderItemReview.getRating(),
                orderItemReview.getComment(),
                orderItemReview.getReviewDate(),
                orderItemReview.getInvsaleUser().getEmail(),
                orderItemReview.getOrderItem().getIdOrderItem()
        );
    }
}
