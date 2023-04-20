package ex.rr.adminpanel.data.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    List<Template> findAllByActive(boolean active);
}
