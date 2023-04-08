package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

/**
 * The {@code DataGrid} class represents DATA_GRID section type specific attributes.
 * <ul>
 *     <li>{@code actions} - List of actions to be bound to each element of the result set. Each element will create a button that executes specified action. </li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     Action
 */
@Data
public class DataGrid {
    List<Action> actions;
}
