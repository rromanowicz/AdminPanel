package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.ActionSubType;
import ex.rr.adminpanel.enums.ActionType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Action {
    private String name;
    private ActionType action;
    private ActionSubType type;
    private String url;
    private List<Map.Entry<String,String>> headers;
    private List<Map.Entry<String,String>> data;
}
