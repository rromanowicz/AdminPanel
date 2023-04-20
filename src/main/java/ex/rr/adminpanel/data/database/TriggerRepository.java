package ex.rr.adminpanel.data.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, String> {
    List<Trigger> findAllByEnabled(boolean enabled);
}
