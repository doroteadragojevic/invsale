package fer.hr.invsale.DAO;

import lombok.*;

import jakarta.persistence.*;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idShelf;
    @NonNull
    private Double dimensionX;
    @NonNull
    private Double dimensionY;
    @ManyToMany
    private Set<ShelfItem> shelfItems;

}
