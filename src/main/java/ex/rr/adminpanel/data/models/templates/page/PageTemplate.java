package ex.rr.adminpanel.data.models.templates.page;

import ex.rr.adminpanel.data.database.Template;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * The {@code PageTemplate} class represents root element of the report template. Is mapped within Template entity object
 * <ul>
 *     <li>{@code name} - Name of the template</li>
 *     <li>{@code owner} - Person responsible for maintenance</li>
 *     <li>{@code visibility} - Defines who should be able to access to this report</li>
 *     <li>{@code sections} - List of sections (sub-reports) to be displayed</li>
 *     <li>{@code globalFilters} - List of filters that will be applied to all child elements of the report</li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     Template
 * @see     PageVisibility
 * @see     PageSection
 * @see     Filter
 */
@Data
public class PageTemplate {

    @NotNull
    private String name;
    private String owner; //TODO change when user entity is created
    private PageVisibility visibility;
    private List<PageSection> sections;
    private List<Filter> globalFilters;

}
