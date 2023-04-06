package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.ActionSubType;
import ex.rr.adminpanel.enums.ActionType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The {@code Action} class represents actions to be added in DATA_GRID.<p>
 * {@code name} - <p>
 * {@code action} - Action to be performed
 * <pre>
 *       Values: LOG, TEAMS_MESSAGE, REST
 *  </pre><p>
 * {@code type} - Type of REST call (required if action == REST)
 * <pre>
 *       Values: TEXT, POST, GET
 * </pre>
 * </pre><p>
 * {@code url} - Url for action with ${VALUE} markers for substitution<p>
 * {@code headers} - Headers to add to the REST call<p>
 * {@code data} - Values for substitution in url<p>
 *
 * @author Robert Romanowicz
 * @see ActionType
 * @see ActionSubType
 */
@Data
public class Action {
    private String name;
    private ActionType action;
    private ActionSubType type;
    private String url;
    private List<Map.Entry<String, String>> headers;
    private List<Map.Entry<String, String>> data;
}
