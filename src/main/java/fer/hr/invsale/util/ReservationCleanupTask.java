package fer.hr.invsale.util;

import fer.hr.invsale.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationCleanupTask {

    @Autowired
    private ReservationService reservationService;

    @Scheduled(fixedRate = 10 * 60 * 1000) // svakih 10 min
    public void cleanUp() {
        reservationService.cleanupExpiredReservations();
    }
}

