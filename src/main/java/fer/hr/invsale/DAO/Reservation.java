package fer.hr.invsale.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Product product;
    @ManyToOne
    private Unit unit;
    private Integer quantity;
    @ManyToOne
    private InvsaleUser user;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    @ManyToOne
    private Order order;

}
