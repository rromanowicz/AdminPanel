package ex.rr.adminpanel.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbTriggerRepository extends JpaRepository<DbTrigger, Long> {
}
