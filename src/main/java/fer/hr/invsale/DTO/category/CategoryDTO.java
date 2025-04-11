package fer.hr.invsale.DTO.category;

import fer.hr.invsale.DAO.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class CategoryDTO {

    @NonNull
    private String name;
    @Nullable
    private String description;

    public static CategoryDTO toDto(Category category) {
        if(category.getDescription() == null) return new CategoryDTO(category.getName());
        return new CategoryDTO(category.getName(), category.getDescription());
    }

}
