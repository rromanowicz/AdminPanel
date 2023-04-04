package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.FilterOperator;
import lombok.Data;

@Data
public class Filter {
    private String column; //TODO Column?
    private FilterOperator operator;
    private String value;
}

