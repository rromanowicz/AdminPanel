package ex.rr.adminpanel.data.models.templates.page;

import ex.rr.adminpanel.data.enums.ColumnType;
import lombok.Data;

/**
 * The {@code Column} class represents column definition.
 * <ul>
 *     <li>{@code name} - Name of the column.</li>
 *     <li>{@code type} - Type of the column.
 *         <pre>Values: TEXT, NUMBER, DATE, DATETIME</pre></li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     ColumnType
 */
@Data
public class Column {
    private String name;
    private ColumnType type;
}
