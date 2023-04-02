package ex.rr.adminpanel.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, String> {
}
