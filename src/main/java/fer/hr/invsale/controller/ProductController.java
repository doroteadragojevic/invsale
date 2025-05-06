package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.DTO.product.ProductDTO;
import fer.hr.invsale.DTO.product.UpdateProductDTO;
import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * REST controller for managing products in the inventory system.
 * Provides endpoints for CRUD operations on products, as well as
 * managing liked products, categories, and units associated with products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * Retrieves a list of all products.
     *
     * @return list of ProductDTO wrapped in ResponseEntity
     */
    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product
     * @return ProductDTO if found, otherwise 404 Not Found
     */

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/price/{id}")
    public ResponseEntity<Double> getPrice(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(productService.getPrice(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/discount/{id}")
    public ResponseEntity<Boolean> getDiscount(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(productService.getDiscount(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/basicUnit/{id}")
    public ResponseEntity<UnitDTO> getBasicUnit(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(productService.getBasicUnit(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reserved/{id}")
    public ResponseEntity<Integer> getReservedQuantity(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getReservedQuantity(id));
    }

    @GetMapping("/rating/{id}")
    public ResponseEntity<OptionalDouble> getRatingByProduct(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(productService.getRatingByProduct(id));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves a list of liked products for a specific user.
     *
     * @param email the email of the user
     * @return list of liked ProductDTOs, or 404 if user not found
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<List<ProductDTO>> getLikedProducts(@PathVariable String email) {
        try {
            return ResponseEntity.ok(productService.getLikedProducts(email));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves categories associated with a given product.
     *
     * @param idProduct the ID of the product
     * @return list of CategoryDTOs, or 404 if product not found
     */
    @GetMapping("/categories/{idProduct}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesForProduct(@PathVariable Integer idProduct) {
        try {
            return ResponseEntity.ok(productService.getCategories(idProduct));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves units associated with a given product.
     *
     * @param idProduct the ID of the product
     * @return list of UnitDTOs, or 404 if product not found
     */
    @GetMapping("/units/{idProduct}")
    public ResponseEntity<List<UnitDTO>> getUnitsForProduct(@PathVariable Integer idProduct) {
        try {
            return ResponseEntity.ok(productService.getUnits(idProduct));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String name) {
        try {
            return ResponseEntity.ok(productService.getProductsByCategory(name));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recommended/{email}")
    public ResponseEntity<List<ProductDTO>> getRecommendedProducts(@PathVariable String email) {
        try {
            return ResponseEntity.ok(productService.getRecommendedProducts(email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/img/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> addImage(@PathVariable Integer id,
                                               @RequestPart("image") MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            productService.setImageData(id, imageFile.getBytes());
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a product to a user's liked products.
     *
     * @param email     the user's email
     * @param idProduct the ID of the product
     * @return 204 No Content if successful, 404 if user or product not found
     */
    @PostMapping("/add/{email}/{idProduct}")
    public ResponseEntity<Void> addToLikedProducts(@PathVariable String email, @PathVariable Integer idProduct) {
        try {
            productService.addToLikedProducts(email, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Removes a product from a user's liked products.
     *
     * @param email     the user's email
     * @param idProduct the ID of the product
     * @return 204 No Content if successful, 404 if user or product not found
     */
    @PostMapping("/remove/{email}/{idProduct}")
    public ResponseEntity<Void> removeFromLikedProducts(@PathVariable String email, @PathVariable Integer idProduct) {
        try {
            productService.removeFromLikedProducts(email, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new product.
     *
     * @param product the ProductDTO containing product information
     * @return created ProductDTO and 201 Created status, or 400 Bad Request if manufacturer does not exist
     */
    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        try {
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    /**
     * Updates an existing product.
     *
     * @param product the UpdateProductDTO with updated product data
     * @return 204 No Content if successful, 404 if product not found
     */
    @PutMapping("/")
    public ResponseEntity<Void> updateProduct(@RequestBody UpdateProductDTO product) {
        try {
            productService.updateProduct(product);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a category to a product.
     *
     * @param idProduct the ID of the product
     * @param name      the name of the category
     * @return 204 No Content if successful, 404 if product not found, 400 if category does not exist
     */
    @PutMapping("/{idProduct}/category/add/{name}")
    public ResponseEntity<Void> addCategory(@PathVariable Integer idProduct, @PathVariable String name) {
        try {
            productService.addCategory(name, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Removes a category from a product.
     *
     * @param idProduct the ID of the product
     * @param name      the name of the category
     * @return 204 No Content if successful, 404 if product not found, 400 if category does not exist
     */
    @PutMapping("/{idProduct}/category/remove/{name}")
    public ResponseEntity<Void> removeCategory(@PathVariable Integer idProduct, @PathVariable String name) {
        try {
            productService.removeCategory(name, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Adds a unit to a product.
     *
     * @param idProduct the ID of the product
     * @param unitId    the ID of the unit
     * @return 204 No Content if successful, 404 if product not found, 400 if unit does not exist
     */
    @PutMapping("/{idProduct}/unit/add/{unitId}")
    public ResponseEntity<Void> addUnit(@PathVariable Integer idProduct, @PathVariable Integer unitId) {
        try {
            productService.addUnit(unitId, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Removes a unit from a product.
     *
     * @param idProduct the ID of the product
     * @param unitId    the ID of the unit
     * @return 204 No Content if successful, 404 if product not found, 400 if unit does not exist
     */
    @PutMapping("/{idProduct}/unit/remove/{unitId}")
    public ResponseEntity<Void> removeUnit(@PathVariable Integer idProduct, @PathVariable Integer unitId) {
        try {
            productService.removeUnit(unitId, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a product by ID.
     *
     * @param id the ID of the product
     * @return 204 No Content if successful, 404 if product not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
