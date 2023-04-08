package ex.rr.adminpanel.database;

import ex.rr.adminpanel.models.templates.page.PageTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * The {@code Template} class represents the template database entity
 * <ul>
 *     <li>{@code id} - Autogenerated id.</li>
 *     <li>{@code template} - JSON string with PageTemplate definition.</li>
 *     <li>{@code @Transient pageTemplate} - Parsed object required for further processing.</li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     PageTemplate
 */
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
