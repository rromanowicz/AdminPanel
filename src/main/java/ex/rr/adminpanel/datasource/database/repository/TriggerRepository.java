package ex.rr.adminpanel.datasource.database.repository;

import ex.rr.adminpanel.datasource.database.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, String> {
}
