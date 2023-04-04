package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.ActionSubType;
import ex.rr.adminpanel.enums.ActionType;
import lombok.Data;

import java.util.Map;

@Data
public class Action {
    private String name;
    private ActionType action;
    private ActionSubType type;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> data;
}
