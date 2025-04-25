package fer.hr.invsale.service;

import fer.hr.invsale.DAO.*;
import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.DTO.product.ProductDTO;
import fer.hr.invsale.DTO.product.UpdateProductDTO;
import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    PriceListRepository priceListRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::toDto).toList();
    }

    public Optional<ProductDTO> getProductById(Integer id) {
        return productRepository.findById(id).map(ProductDTO::toDto);
    }

    public List<ProductDTO> getLikedProducts(String email) throws NoSuchObjectException {
        if (!invsaleUserRepository.existsById(email))
            throw new NoSuchObjectException("User with email " + email + " does not exist.");
        if (invsaleUserRepository.findById(email).get().getLikedProducts() != null)
            return invsaleUserRepository.findById(email).get().getLikedProducts().stream().map(ProductDTO::toDto).toList();
        return List.of();
    }

    public List<CategoryDTO> getCategories(Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        return productRepository.findById(idProduct).get().getCategories().stream().map(CategoryDTO::toDto).toList();
    }

    public List<UnitDTO> getUnits(Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        return productRepository.findById(idProduct).get().getQuantityUnits().stream().map(UnitDTO::toDto).toList();
    }

    public void addToLikedProducts(String email, Integer idProduct) throws NoSuchObjectException {
        if (!invsaleUserRepository.existsById(email)
                || !productRepository.existsById(idProduct))
            throw new NoSuchObjectException("User/product does not exist.");
        InvsaleUser user = invsaleUserRepository.findById(email).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        Set<Product> likedProducts = user.getLikedProducts();
        if (likedProducts != null)
            likedProducts.add(product);
        else
            likedProducts = Set.of(product);
        user.setLikedProducts(likedProducts);
        invsaleUserRepository.save(user);
    }

    public void removeFromLikedProducts(String email, Integer idProduct) throws NoSuchObjectException {
        if (!invsaleUserRepository.existsById(email)
                || !productRepository.existsById(idProduct))
            throw new NoSuchObjectException("User/product does not exist.");
        InvsaleUser user = invsaleUserRepository.findById(email).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        Set<Product> likedProducts = user.getLikedProducts();
        if (likedProducts != null)
            likedProducts.remove(product);
        else
            likedProducts = new HashSet<>();
        user.setLikedProducts(likedProducts);
        invsaleUserRepository.save(user);
    }

    public ProductDTO createProduct(ProductDTO product) {
        if ((product.getIdManufacturer() != null && !manufacturerRepository.existsById(product.getIdManufacturer())))
            throw new IllegalArgumentException("Manufacurer with id " + product.getIdManufacturer() + " does not exist.");
        return ProductDTO.toDto(createFromDto(product));
    }

    private Product createFromDto(ProductDTO product) {
        Product newProduct = new Product();
        Optional.ofNullable(product.getIdManufacturer()).ifPresent(man -> {
            Manufacturer manufacturer = manufacturerRepository.findById(man).orElseThrow(NullPointerException::new);
            newProduct.setManufacturer(manufacturer);
        });
        newProduct.setName(product.getName());
        Optional.ofNullable(product.getIngredients()).ifPresent(newProduct::setIngredients);
        newProduct.setDescription(product.getDescription());
        Optional.ofNullable(product.getReorderNotificationThreshold()).ifPresent(newProduct::setReorderNotificationThreshold);
        newProduct.setQuantityOnStock(product.getQuantityOnStock());
        return productRepository.save(newProduct);
    }

    public void updateProduct(UpdateProductDTO product) throws NoSuchObjectException {
        if (!productRepository.existsById(product.getIdProduct()))
            throw new NoSuchObjectException("Product with id " + product.getIdProduct() + " does not exist.");
        if (!manufacturerRepository.existsById(product.getIdManufacturer()))
            throw new NoSuchObjectException("Manufacturer with id " + product.getIdProduct() + " does not exist.");
        updateFromDto(product);
    }

    private void updateFromDto(UpdateProductDTO product) {
        Product updateProduct = productRepository.findById(product.getIdProduct()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(product.getIdManufacturer()).ifPresent(man -> {
            Manufacturer manufacturer = manufacturerRepository.findById(man).orElseThrow(NullPointerException::new);
            updateProduct.setManufacturer(manufacturer);
        });
        Optional.ofNullable(product.getName()).ifPresent(updateProduct::setName);
        Optional.ofNullable(product.getIngredients()).ifPresent(updateProduct::setIngredients);
        Optional.ofNullable(product.getDescription()).ifPresent(updateProduct::setDescription);
        Optional.ofNullable(product.getReorderNotificationThreshold()).ifPresent(updateProduct::setReorderNotificationThreshold);
        Optional.ofNullable(product.getQuantityOnStock()).ifPresent(updateProduct::setQuantityOnStock);
        productRepository.save(updateProduct);
    }

    public void addCategory(String categoryName, Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        if (!categoryRepository.existsById(categoryName))
            throw new IllegalArgumentException("Category " + categoryName + " does not exist.");
        Category category = categoryRepository.findById(categoryName).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        if (product.getCategories() == null)
            product.setCategories(Set.of(category));
        else {
            Set<Category> categories = product.getCategories();
            categories.add(category);
            product.setCategories(categories);
        }
        productRepository.save(product);

    }

    public void removeCategory(String categoryName, Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        if (!categoryRepository.existsById(categoryName))
            throw new IllegalArgumentException("Category " + categoryName + " does not exist.");
        Category category = categoryRepository.findById(categoryName).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        if (product.getCategories() != null) {
            Set<Category> categories = product.getCategories();
            categories.remove(category);
            product.setCategories(categories);
            productRepository.save(product);
        }
    }

    public void addUnit(Integer unitId, Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        if (!unitRepository.existsById(unitId))
            throw new IllegalArgumentException("Unit with id  " + unitId + " does not exist.");
        Unit unit = unitRepository.findById(unitId).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        if (product.getQuantityUnits() == null)
            product.setQuantityUnits((TreeSet<Unit>) Set.of(unit));
        else {
            SortedSet<Unit> units = product.getQuantityUnits();
            units.add(unit);
            product.setQuantityUnits(units);
        }
        productRepository.save(product);
    }

    public void removeUnit(Integer unitId, Integer idProduct) throws NoSuchObjectException {
        if (!productRepository.existsById(idProduct))
            throw new NoSuchObjectException("Product with id " + idProduct + " does not exist.");
        if (!unitRepository.existsById(unitId))
            throw new IllegalArgumentException("Unit with id  " + unitId + " does not exist.");
        Unit unit = unitRepository.findById(unitId).orElseThrow(NullPointerException::new);
        Product product = productRepository.findById(idProduct).orElseThrow(NullPointerException::new);
        if (product.getQuantityUnits() != null) {
            SortedSet<Unit> units = product.getQuantityUnits();
            units.remove(unit);
            product.setQuantityUnits(units);
            productRepository.save(product);
        }
    }

    @Transactional
    public void deleteProduct(Integer id) throws NoSuchObjectException {
        if (!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id + " does not exist.");
        orderItemRepository.deleteAllByProduct_IdProduct(id);
        priceListRepository.deleteAllByProduct_IdProduct(id);
        productRepository.deleteById(id);
    }

    public void setImageData(Integer id, byte[] bytes) {
        // Spremi proizvod u bazu
        Product savedProduct = productRepository.findById(id).get();
        savedProduct.setImageData(bytes);
        productRepository.save(savedProduct);
    }

    public List<ProductDTO> getProductsByCategory(String name) {
        Category category = categoryRepository.findById(name).orElseThrow(IllegalAccessError::new);
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getCategories().contains(category))
                .map(ProductDTO::toDto).toList();
    }

    public Double getPrice(Integer id) throws NoSuchObjectException {
        if(!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id  + " does not exist.");
        Product product = productRepository.findById(id).get();
        Unit unit = product.getQuantityUnits().first();
        PriceList priceList = priceListRepository.findByProductAndUnit(product, unit).orElseThrow(IllegalArgumentException::new);

        return priceList.getDiscount() == null ?
                priceList.getPriceWithoutDiscount() :
                priceList.getPriceWithoutDiscount() * (1 - priceList.getDiscount());


    }
}

