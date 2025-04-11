package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.List;

/**
 * REST controller for managing product categories.
 * <p>
 * This controller provides CRUD operations for categories,
 * including retrieving all categories, retrieving a category by ID,
 * creating a new category, updating an existing category, and deleting a category.
 * </p>
 *
 * <p>
 * All responses are returned as {@link org.springframework.http.ResponseEntity},
 * and data objects use the DTO class {@code CategoryDTO}.
 * </p>
 *
 * Example routes:
 * <ul>
 *     <li><b>GET</b> /api/categories/ – Retrieves all categories</li>
 *     <li><b>GET</b> /api/categories/{name} – Retrieves a category by ID</li>
 *     <li><b>POST</b> /api/categories/ – Creates a new category</li>
 *     <li><b>PUT</b> /api/categories/ – Updates an existing category</li>
 *     <li><b>DELETE</b> /api/categories/{name} – Deletes a category</li>
 * </ul>
 *
 * @author Dorotea Dragojević
 * @version 1.0
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * Handles HTTP GET request to retrieve all categories.
     *
     * @return ResponseEntity containing the list of all CategoryDTOs.
     */
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Handles HTTP GET request to retrieve a category by its name.
     *
     * @param name the name of the category to retrieve
     * @return ResponseEntity containing the CategoryDTO if found, or 404 Not Found if not found
     */
    @GetMapping("/{name}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String name) {
        return categoryService.getCategoryById(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Handles HTTP POST request to create a new category.
     *
     * @param category the CategoryDTO containing data for the new category
     * @return ResponseEntity containing the created CategoryDTO with status 201 Created,
     *         or 409 Conflict if the category already exists
     */
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO category){
        try{
            return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
        }catch(KeyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Handles HTTP PUT request to update an existing category.
     *
     * @param category the CategoryDTO containing updated data
     * @return ResponseEntity with status 204 No Content if update was successful,
     *         or 404 Not Found if the category does not exist
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDTO category) {
        try{
            categoryService.updateCategory(category);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles HTTP DELETE request to delete a category by its name.
     *
     * @param name the name of the category to delete
     * @return ResponseEntity with status 204 No Content if deletion was successful,
     *         or 404 Not Found if the category does not exist
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        try{
            categoryService.deleteCategory(name);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
