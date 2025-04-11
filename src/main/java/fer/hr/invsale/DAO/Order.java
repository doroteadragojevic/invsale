package fer.hr.invsale.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
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
