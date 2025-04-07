package fer.hr.invsale.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrder;
    @NonNull
    private Double totalPrice;
    @ManyToOne
    @NonNull
    private OrderStatus orderStatus;
    @NonNull
    private Timestamp orderTimestamp;
    @ManyToOne
    @NonNull
    private InvsaleUser invsaleUser;
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Nullable
    private String shippingAddress;


}
