package fer.hr.invsale.DTO.review;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UpdateOrderReviewDTO {
    @NonNull
    private Integer idReview;
    @NonNull
    private String comment;
}
