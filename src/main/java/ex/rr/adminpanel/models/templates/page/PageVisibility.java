package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.VisibilityType;
import lombok.Data;

@Data
public class PageVisibility {
    private VisibilityType type;
    private String value; //TODO enum?
}
