package fer.hr.invsale.service;

import fer.hr.invsale.DAO.*;
import fer.hr.invsale.DTO.category.CategoryDTO;
import fer.hr.invsale.DTO.product.ProductDTO;
import fer.hr.invsale.DTO.product.UpdateProductDTO;
import fer.hr.invsale.DTO.review.OrderItemReviewDTO;
import fer.hr.invsale.DTO.unit.UnitDTO;
import fer.hr.invsale.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    PriceListRepository priceListRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemReviewService orderItemReviewService;

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
        if (product.getIdManufacturer() != null && !manufacturerRepository.existsById(product.getIdManufacturer()))
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
        if (!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id + " does not exist.");
        Product product = productRepository.findById(id).get();
        Set<Unit> units = product.getQuantityUnits();
        PriceList priceListLowest = null;
        Double min = Double.MAX_VALUE;
        for (Unit unit : units) {
            PriceList priceList = priceListRepository.findByProductAndUnit(product, unit).orElseThrow(IllegalArgumentException::new);
            if (priceList.getPriceWithoutDiscount() < min && isActive(priceList)) {
                min = priceList.getPriceWithoutDiscount();
                priceListLowest = priceList;
            }
        }

        return priceListLowest.getDiscount() == null ?
                priceListLowest.getPriceWithoutDiscount() :
                priceListLowest.getPriceWithoutDiscount() * (1 - priceListLowest.getDiscount());


    }

    private boolean isActive(PriceList priceList) {
        return priceList.getDateTimeFrom().before(Timestamp.valueOf(LocalDateTime.now()))
                && priceList.getDateTimeTo().after(Timestamp.valueOf(LocalDateTime.now()));
    }

    public OptionalDouble getRatingByProduct(Integer id) throws NoSuchObjectException {
        if (!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id + " does not exist.");
        List<OrderItemReviewDTO> reviews = orderItemReviewService.getReviewsForProduct(id);
        return reviews.stream().mapToInt(review -> review.getRating().getValue()).average();
    }

    public Boolean getDiscount(Integer id) throws NoSuchObjectException {
        if (!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id + " does not exist.");
        Product product = productRepository.findById(id).get();
        Set<Unit> units = product.getQuantityUnits();
        PriceList priceListLowest = null;
        Double min = Double.MAX_VALUE;
        for (Unit unit : units) {
            PriceList priceList = priceListRepository.findByProductAndUnit(product, unit).orElseThrow(IllegalArgumentException::new);
            if (priceList.getPriceWithoutDiscount() < min && isActive(priceList)) {
                min = priceList.getPriceWithoutDiscount();
                priceListLowest = priceList;
            }
        }

        return priceListLowest.getDiscount() != null && priceListLowest.getDiscount() != 0;
    }

    public UnitDTO getBasicUnit(Integer id) throws NoSuchObjectException {
        if (!productRepository.existsById(id))
            throw new NoSuchObjectException("Product with id " + id + " does not exist.");
        Product product = productRepository.findById(id).get();
        Set<Unit> units = product.getQuantityUnits();
        PriceList priceListLowest = null;
        Double min = Double.MAX_VALUE;
        for (Unit unit : units) {
            PriceList priceList = priceListRepository.findByProductAndUnit(product, unit).orElseThrow(IllegalArgumentException::new);
            if (priceList.getPriceWithoutDiscount() < min && isActive(priceList)) {
                min = priceList.getPriceWithoutDiscount();
                priceListLowest = priceList;
            }
        }

        return UnitDTO.toDto(priceListLowest.getUnit());
    }

    public Integer getReservedQuantity(Integer id, Integer unitId) {
        return reservationService.getCurrentlyReservedQuantity(id, unitId);
    }

    public List<ProductDTO> getRecommendedProducts(String email) {
        List<Product> productsUserOrdered = orderItemRepository
                .findAll()
                .stream().filter(orderItem -> !orderItem.getOrder().getOrderStatus().getName().equals("IN_PROGRESS"))
                .filter(orderItem -> orderItem.getOrder().getInvsaleUser().getEmail().equals(email))
                .map(orderItem -> orderItem.getProduct())
                .collect(Collectors.toCollection(ArrayList::new));

        Set<String> ingredientsInOrderedProducts = new HashSet<>();
        productsUserOrdered.forEach(product -> {
            if (product.getIngredients() != null)
                ingredientsInOrderedProducts.addAll(product.getIngredients());
        });

        List<Product> recommended = productRepository.findAll().stream()
                // filtriraj one koji imaju barem jedan ingredient iz korisnikovih naručenih proizvoda
                .filter(product -> {
                    if (product.getIngredients() == null) return false;
                    for (String ingredient : product.getIngredients()) {
                        if (ingredientsInOrderedProducts.contains(ingredient)) {
                            return true;
                        }
                    }
                    return false;
                })
                // sortiraj po broju matching sastojaka — od najmanjeg prema najvećem
                .sorted(Comparator.comparingInt((Product product) -> {
                    long count = product.getIngredients().stream()
                            .filter(ingredientsInOrderedProducts::contains)
                            .count();
                    return (int) count;
                }).reversed())
                .collect(Collectors.toCollection(ArrayList::new));

        int i = 0;
        List<Product> popularProducts = getPopularProducts();
        while(recommended.size() < 6){
            recommended.add(popularProducts.get(i++));
        }

        return recommended.stream().map(ProductDTO::toDto).toList();

    }

    private List<Product> getPopularProducts() {
        List<OrderItem> allOrderItems = orderItemRepository.findAll();

        // Broji koliko puta se svaki proizvod pojavio u narudžbama
        Map<Product, Long> productCountMap = allOrderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.counting()));

        // Sortira proizvode prema broju kupnji (od najviše prema najmanje) i vraća ih kao listu
        return productCountMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());
    }
}

