package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

@Data
public class PageTemplate {

    private String name;
    private String owner; //TODO change when user entity is created
    private PageVisibility visibility;
    private List<PageSection> sections;
    private List<Filter> globalFilters;

}
