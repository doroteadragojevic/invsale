package fer.hr.invsale.DTO.review;

import fer.hr.invsale.DAO.OrderReview;
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
public class OrderReviewDTO {
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
    private Integer orderId;

    public static OrderReviewDTO toDto(OrderReview orderReview) {
        return new OrderReviewDTO(
                orderReview.getIdReview(),
                orderReview.getRating(),
                orderReview.getComment(),
                orderReview.getReviewDate(),
                orderReview.getInvsaleUser().getEmail(),
                orderReview.getOrder().getIdOrder()
        );
    }

}
