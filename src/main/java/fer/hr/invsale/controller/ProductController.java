package fer.hr.invsale.controller;

import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.DTO.product.ProductDTO;
import fer.hr.invsale.DTO.product.UpdateProductDTO;
import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.NoSuchObjectException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<ProductDTO>> getLikedProducts(@PathVariable String email) {
        try {
            return ResponseEntity.ok(productService.getLikedProducts(email));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categories/{idProduct}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesForProduct(@PathVariable Integer idProduct) {
        try {
            return ResponseEntity.ok(productService.getCategories(idProduct));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/units/{idProduct}")
    public ResponseEntity<List<UnitDTO>> getUnitsForProduct(@PathVariable Integer idProduct) {
        try {
            return ResponseEntity.ok(productService.getUnits(idProduct));
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add/{email}/{idProduct}")
    public ResponseEntity<Void> addToLikedProducts(@PathVariable String email, @PathVariable Integer idProduct) {
        try {
            productService.addToLikedProducts(email, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/remove/{email}/{idProduct}")
    public ResponseEntity<Void> removeFromLikedProducts(@PathVariable String email, @PathVariable Integer idProduct) {
        try {
            productService.removeFromLikedProducts(email, idProduct);
            return ResponseEntity.noContent().build();
        } catch (NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product) {
        try {
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping("/")
    public ResponseEntity<Void> updateProduct(@RequestBody UpdateProductDTO product) {
        try{
            productService.updateProduct(product);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        try{
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchObjectException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
