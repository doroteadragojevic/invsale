package fer.hr.invsale.repository;

import fer.hr.invsale.DAO.InvsaleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvsaleUserRepository extends JpaRepository<InvsaleUser, String> {
}
