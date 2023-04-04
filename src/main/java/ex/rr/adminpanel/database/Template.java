package ex.rr.adminpanel.database;

import ex.rr.adminpanel.models.templates.page.PageTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Template {

    @Id
    @GeneratedValue
    private Long id;

    private String template;

    @Transient
    private PageTemplate pageTemplate;

}
