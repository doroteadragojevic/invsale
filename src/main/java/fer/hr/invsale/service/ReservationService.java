package fer.hr.invsale.service;

import fer.hr.invsale.DAO.InvsaleUser;
import fer.hr.invsale.DAO.Product;
import fer.hr.invsale.DAO.Reservation;
import fer.hr.invsale.repository.InvsaleUserRepository;
import fer.hr.invsale.repository.ProductRepository;
import fer.hr.invsale.repository.ReservationRepository;
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

    public void reserveProduct(Integer productId, String email, Integer quantity) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiresAt = new Timestamp(now.getTime() + 60 * 60 * 1000); // 1h
        Reservation reservation = new Reservation();
        Product product = productRepository.findById(productId).get();
        reservation.setProduct(product); // ili dohvaćen preko ProductService
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        reservation.setUser(user);
        reservation.setQuantity(quantity);
        reservation.setCreatedAt(now);
        reservation.setExpiresAt(expiresAt);

        reservationRepository.save(reservation);
    }

    public Integer getCurrentlyReservedQuantity(Integer productId) {
        Integer reserved = 0;
        reserved = reservationRepository.findAllByProduct_IdProduct(productId).stream().mapToInt(Reservation::getQuantity).sum();
        return reserved;
    }

    public void cleanupExpiredReservations() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Reservation> expired = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getExpiresAt().before(now))
                .toList();
        reservationRepository.deleteAll(expired);
    }

    public void updateReservation(Integer idProduct,  String email, int addition) {
        Product product = productRepository.findById(idProduct).get();
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        Reservation reservation = reservationRepository.findByProductAndUser(product, user).get();
        reservation.setQuantity(reservation.getQuantity() + addition);
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Integer idProduct,  String email) {
        Product product = productRepository.findById(idProduct).get();
        InvsaleUser user = invsaleUserRepository.findById(email).get();
        Reservation reservation = reservationRepository.findByProductAndUser(product, user).get();
        reservationRepository.delete(reservation);
    }
}
