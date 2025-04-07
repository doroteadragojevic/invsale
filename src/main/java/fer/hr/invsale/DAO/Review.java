package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;


@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReview;
    @NonNull
    @Enumerated(value = EnumType.STRING)
    private Rating rating;
    @Nullable
    private String comment;
    @NonNull
    private Date reviewDate;
    @ManyToOne
    @NonNull
    private InvsaleUser invsaleUser;

}
