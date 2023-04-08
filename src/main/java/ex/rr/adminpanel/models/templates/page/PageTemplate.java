package ex.rr.adminpanel.models.templates.page;

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
 * @author Robert Romanowicz
 * @see ex.rr.adminpanel.database.Template
 * @see PageVisibility
 * @see PageSection
 * @see Filter
 */
@Data
public class PageTemplate {

    private String name;
    private String owner; //TODO change when user entity is created
    private PageVisibility visibility;
    private List<PageSection> sections;
    private List<Filter> globalFilters;

}
