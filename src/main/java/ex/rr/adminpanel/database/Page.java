package ex.rr.adminpanel.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Page {

    @Id
    @GeneratedValue
    private Long id;

    private String template;

}
