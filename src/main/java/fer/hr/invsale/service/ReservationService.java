package fer.hr.invsale.service;

import fer.hr.invsale.DAO.*;
import fer.hr.invsale.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InvsaleUserRepository invsaleUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UnitRepository unitRepository;

    public void reserveProduct(Integer productId, String email, Integer quantity, Integer unidId, Integer orderId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiresAt = new Timestamp(now.getTime() + 60 * 60 * 1000); // 1h
        Reservation reservation = new Reservation();
        Product product = productRepository.findById(productId).get();
        reservation.setProduct(product); // ili dohvaćen preko ProductService
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        Unit unit = unitRepository.findById(unidId).get();
        reservation.setUnit(unit);
        reservation.setUser(user);
        Order order = orderRepository.findById(orderId).get();
        reservation.setOrder(order);
        reservation.setQuantity(quantity);
        reservation.setCreatedAt(now);
        reservation.setExpiresAt(expiresAt);
        reservationRepository.save(reservation);
    }

    public Integer getCurrentlyReservedQuantity(Integer productId, Integer unitId) {
        Integer reserved = 0;
        reserved = reservationRepository.findAllByProduct_IdProductAndUnit_IdUnit(productId, unitId).stream().mapToInt(Reservation::getQuantity).sum();
        return reserved;
    }

    public void cleanupExpiredReservations() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Reservation> expired = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getExpiresAt().before(now))
                .toList();
        reservationRepository.deleteAll(expired);
        List<Integer> orderItemIds = expired.stream().map(reservation ->
             orderItemRepository.findByProductAndUnitAndOrder(reservation.getProduct(), reservation.getUnit(), reservation.getOrder()).get()
        ).map(orderItem -> orderItem.getIdOrderItem()).toList();
        orderItemRepository.deleteAllById(orderItemIds);
    }

    public void updateReservation(Integer idProduct, String email, int addition, Integer unitId) {
        Product product = productRepository.findById(idProduct).get();
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        Unit unit = unitRepository.findById(unitId).get();
        Reservation reservation = reservationRepository.findByProductAndUserAndUnit(product, user, unit).get();
        reservation.setQuantity(reservation.getQuantity() + addition);
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Integer idProduct,  String email, Integer unitId) {
        Product product = productRepository.findById(idProduct).get();
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        Unit unit = unitRepository.findById(unitId).get();
        reservationRepository.findByProductAndUserAndUnit(product, user, unit).ifPresent(reservationRepository::delete);
    }

    public void deleteAllByOrderId(@NotNull Integer idOrder) {
        reservationRepository.deleteAllByOrder_IdOrder(idOrder);
    }
}
