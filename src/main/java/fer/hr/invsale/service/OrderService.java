package fer.hr.invsale.service;

import fer.hr.invsale.DAO.*;
import fer.hr.invsale.DTO.StatsDTO;
import fer.hr.invsale.DTO.order.CreateOrderDTO;
import fer.hr.invsale.DTO.order.OrderDTO;
import fer.hr.invsale.DTO.order.UpdateOrderDTO;
import fer.hr.invsale.DTO.stock.WarehouseStatsDTO;
import fer.hr.invsale.DTO.stock.ZoneProductsDTO;
import fer.hr.invsale.repository.*;
import io.micrometer.observation.ObservationFilter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    InvsaleUserRepository invsaleUserRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderReviewService orderReviewService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CouponService couponService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    EmailService emailService;

    public List<OrderDTO> getAllOrdersByUser(String email) {
        if (!invsaleUserRepository.existsById(email)) throw new IllegalArgumentException("User does not exist.");
        return orderRepository.findAllByInvsaleUser_Email(email).stream().map(OrderDTO::toDto).toList();
    }

    public Optional<OrderDTO> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).map(OrderDTO::toDto);
    }

    public OrderDTO createOrder(CreateOrderDTO order) throws InstanceAlreadyExistsException {
        if (orderInProgressExists(order)) throw new InstanceAlreadyExistsException();
        return createFromDto(order);
    }

    private OrderDTO createFromDto(CreateOrderDTO order) {
        Order createdOrder = new Order();
        OrderStatus orderStatus = orderStatusRepository.findById("IN_PROGRESS").orElseThrow(NullPointerException::new);
        createdOrder.setOrderStatus(orderStatus);
        InvsaleUser invsaleUser = invsaleUserRepository.findById(order.getEmail()).orElseThrow(NullPointerException::new);
        createdOrder.setInvsaleUser(invsaleUser);
        createdOrder.setPaymentMethod(order.getPaymentMethod());
        createdOrder.setShippingAddress(order.getShippingAddress());
        createdOrder.setOrderTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        createdOrder.setTotalPrice(0.);
        return OrderDTO.toDto(
                orderRepository.save(createdOrder));

    }

    private boolean orderInProgressExists(CreateOrderDTO order) {
        return orderRepository.findAllByInvsaleUser_Email(order.getEmail())
                .stream()
                .anyMatch(o -> o.getOrderStatus().getName().equals("IN_PROGRESS"));

    }

    public void updateOrder(UpdateOrderDTO order) throws NoSuchObjectException {
        if (!orderRepository.existsById(order.getIdOrder()))
            throw new NoSuchObjectException("Order with id " + order.getIdOrder() + " does not exist.");
        updateFromDto(order);
    }

    private void updateFromDto(UpdateOrderDTO order) {
        Order updateOrder = orderRepository.findById(order.getIdOrder()).orElseThrow(NullPointerException::new);
        Optional.ofNullable(order.getPaymentMethod()).ifPresent(updateOrder::setPaymentMethod);
        Optional.ofNullable(order.getShippingAddress()).ifPresent(updateOrder::setShippingAddress);
        orderRepository.save(updateOrder);

    }

    @Transactional
    public void deleteOrder(Integer id) throws NoSuchObjectException {
        if (!orderRepository.existsById(id))
            throw new NoSuchObjectException("Object with id " + id + " does not exist.");
        orderItemRepository.deleteAllByOrder_IdOrder(id);
        orderReviewService.deleteAllByOrder_IdOrder(id);
        orderRepository.deleteById(id);
    }

    public Optional<OrderDTO> getCart(String email) {
        return orderRepository.findAllByInvsaleUser_Email(email).stream().filter(order -> order.getOrderStatus().getName().equalsIgnoreCase("IN_PROGRESS")).findFirst().map(OrderDTO::toDto);
    }

    public OrderDTO applyCoupon(Integer idOrder, String code) throws NoSuchObjectException {
        if (orderRepository.findById(idOrder).isPresent()) {
            Order order = orderRepository.findById(idOrder).get();
            if (couponRepository.findById(code).isPresent()) {
                Coupon coupon = couponRepository.findById(code).get();
                Double totalPrice = order.getTotalPrice() * (1 - coupon.getDiscount());
                order.setTotalPrice(totalPrice);
                couponService.couponApplied(order.getInvsaleUser().getEmail(), code);
                return OrderDTO.toDto(orderRepository.save(order));
            } else {
                throw new NoSuchObjectException("Coupon with code " + code + " does not exist.");
            }
        } else {
            throw new NoSuchObjectException("Order with id " + idOrder + " does not exist.");
        }
    }

    public OrderDTO removeCoupon(Integer idOrder, String code) throws NoSuchObjectException {
        if (orderRepository.findById(idOrder).isPresent()) {
            Order order = orderRepository.findById(idOrder).get();
            if (couponRepository.findById(code).isPresent()) {
                Coupon coupon = couponRepository.findById(code).get();
                Double totalPrice = order.getTotalPrice() / (1 - coupon.getDiscount());
                order.setTotalPrice(totalPrice);
                couponService.couponRemoved(order.getInvsaleUser().getEmail(), code);
                return OrderDTO.toDto(orderRepository.save(order));
            } else {
                throw new NoSuchObjectException("Coupon with code " + code + " does not exist.");
            }
        } else {
            throw new NoSuchObjectException("Order with id " + idOrder + " does not exist.");
        }
    }

    @Transactional
    public OrderDTO placeOrder(UpdateOrderDTO order) {
        Order placedOrder = orderRepository.findById(order.getIdOrder()).orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = orderStatusRepository.findById("PLACED").orElseThrow(NullPointerException::new);
        placedOrder.setOrderStatus(orderStatus);
        placedOrder.setPaymentMethod(PaymentMethod.CASH);
        placedOrder.setShippingAddress(order.getShippingAddress());
        placedOrder.setOrderTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        updateQuantity(order);
        reservationService.deleteAllByOrderId(order.getIdOrder());
        placedOrder = orderRepository.save(placedOrder);
        emailService.sendSimpleEmail(placedOrder);
        return OrderDTO.toDto(placedOrder);
    }

    private void updateQuantity(UpdateOrderDTO order) {
        orderItemRepository
                .findAllByOrder_IdOrder(order.getIdOrder())
                .forEach(orderItem -> {
                    Product product = orderItem.getProduct();
                    product.setQuantityOnStock(product.getQuantityOnStock() - orderItem.getQuantity());
                    productRepository.save(product);
                });
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(OrderDTO::toDto)
                .filter(order -> !order.getOrderStatusName()
                        .equals("IN_PROGRESS")).toList();
    }

    public void updateStatus(Integer idOrder, String status) throws NoSuchObjectException {
        if (!orderRepository.existsById(idOrder) || !orderStatusRepository.existsById(status))
            throw new NoSuchObjectException("Status or order does not exist.");
        OrderStatus orderStatus = orderStatusRepository.findById(status).get();
        Order order = orderRepository.findById(idOrder).get();
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    public ResponseEntity<List<StatsDTO>> getStatsInRange(String range, String category, Integer product) {
        List<StatsDTO> stats = new ArrayList<>();
        DateTimeFormatter formatter;

        if ("week".equalsIgnoreCase(range)) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.");
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                int count = countOrdersOnDate(date, category, product);
                stats.add(new StatsDTO(date.format(formatter), count));
            }
        } else if ("month".equalsIgnoreCase(range)) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.");
            for (int i = 29; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                int count = countOrdersOnDate(date, category, product);
                stats.add(new StatsDTO(date.format(formatter), count));
            }
        } else if ("year".equalsIgnoreCase(range)) {
            formatter = DateTimeFormatter.ofPattern("MM.yyyy");
            int currentYear = LocalDate.now().getYear();
            for (int month = 1; month <= 12; month++) {
                int count = countOrdersInMonth(month, currentYear, category, product);
                stats.add(new StatsDTO(month + "." + currentYear, count));
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stats);
    }

    private boolean filterByCategoryAndProduct(Order order, String category, Integer product) {
        if(category!= null && !orderItemRepository
                .findAllByOrder_IdOrder(order.getIdOrder())
                .stream()
                .anyMatch(orderItem -> orderItem.getProduct().getCategories().stream().map(c -> c.getName()).toList()
                        .contains(category))) {
            return false;
        }

        if(product!= null && !orderItemRepository
                .findAllByOrder_IdOrder(order.getIdOrder())
                .stream()
                .anyMatch(orderItem -> orderItem.getProduct().getIdProduct().equals(product))) {
            return false;
        }
        return true;
    }

    private int countOrdersInMonth(int month, int currentYear, String category, Integer product) {
        return (int) orderRepository.findAll().stream().filter(order ->
        {
            if(!(category == null && product == null) && !filterByCategoryAndProduct(order, category, product)) {
                return false;
            }
            Timestamp timestamp = order.getOrderTimestamp();
            return timestamp.getMonth()+1 == month;
        }).count();
    }

    private int countOrdersOnDate(LocalDate date, String category, Integer product) {
        return (int) orderRepository.findAll().stream().filter(order ->
        {
            if( !(category == null && product == null) && !filterByCategoryAndProduct(order, category, product)) {
                return false;
            }
            Timestamp timestamp = order.getOrderTimestamp();
            LocalDate ld = LocalDate.of(2025, timestamp.getMonth()+1, timestamp.getDate());
            return date.equals(ld);
        }).count();
    }

    public List<WarehouseStatsDTO> getWarehouseStats() {
        List<Product> allProducts = productRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfLast3Months = now.minusMonths(3).withDayOfMonth(1).toLocalDate().atStartOfDay();


        List<WarehouseStatsDTO> stats = new ArrayList<>();

        for (Product product : allProducts) {
            int totalQuantityLast3Months = orderItemRepository.sumQuantityByProductIdAndDateRange(
                    product.getIdProduct(), startOfLast3Months, now
            );

            int predicted = Math.round(totalQuantityLast3Months / 3.0f);
            int currentStock = product.getQuantityOnStock();

            WarehouseStatsDTO dto = new WarehouseStatsDTO();
            dto.setProductId(product.getIdProduct());
            dto.setProductName(product.getName());
            dto.setPredictedNextMonthDemand(predicted);
            dto.setCurrentStock(currentStock);
            dto.setReorderNeeded(currentStock < predicted);

            stats.add(dto);
        }

        return stats;
    }

    public List<ZoneProductsDTO> getZoneProducts() {
        LocalDateTime start = LocalDate.now().minusMonths(3).withDayOfMonth(1).atStartOfDay();
        List<Object[]> popularities = orderItemRepository.findProductPopularityLast3Months(start);

        int total = popularities.size();
        int firstThird = total / 3;
        int secondThird = 2 * total / 3;

        List<ZoneProductsDTO> zones = new ArrayList<>();
        zones.add(new ZoneProductsDTO("closest", popularities.subList(0, firstThird).stream().map(p -> (String)p[0]).toList()));
        zones.add(new ZoneProductsDTO("middle", popularities.subList(firstThird, secondThird).stream().map(p -> (String)p[0]).toList()));
        zones.add(new ZoneProductsDTO("farthest", popularities.subList(secondThird, total).stream().map(p -> (String)p[0]).toList()));

        return zones;
    }




}
