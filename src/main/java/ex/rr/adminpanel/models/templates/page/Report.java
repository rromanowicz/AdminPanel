package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

@Data
public class Report {
    private List<String> dimensions;
    private List<String> facts;
    private List<Filter> defaultFilters;
}
