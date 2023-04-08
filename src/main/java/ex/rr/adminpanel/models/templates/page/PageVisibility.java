package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.VisibilityType;
import lombok.Data;

/**
 * The {@code PageVisibility} class represents reports access definition.
 * <ul>
 *     <li>{@code type} - Type of the column.
 *         <pre>Values: PRIVATE, PUBLIC, GROUP</pre></li>
 *     <li>{@code value} - Required for GROUP type (specifies group name). </li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     VisibilityType
 */
@Data
public class PageVisibility {
    private VisibilityType type;
    private String value; //TODO enum?
}
