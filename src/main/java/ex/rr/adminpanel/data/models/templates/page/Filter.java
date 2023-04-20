package ex.rr.adminpanel.data.models.templates.page;

import ex.rr.adminpanel.data.enums.ColumnType;
import ex.rr.adminpanel.data.enums.FilterOperator;
import lombok.Data;


/**
 * The {@code Filter} class represents global filter section.
 * <ul>
 *     <li>{@code column} - name of the column to be used</li>
 *     <li>{@code operator} - logical operator
 *         <pre>Values: EQUAL, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL</pre></li>
 *     <li>{@code value} - value to be used in filter (for DATE and DATETIME, a relative value TODAY{+/-x} can be used). For example:
 *         <ul>
 *             <li>String value = "2023-04-06"</li>
 *             <li>String value = "TODAY"</li>
 *             <li>String value = "TODAY-7"</li>
 *         </ul></li>
 *     <li>{@code columnType} - type of the column
 *         <pre>Values: TEXT, NUMBER, DATE, DATETIME</pre></li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     ColumnType
 * @see     FilterOperator
 */
@Data
public class Filter {

    private String column;
    private FilterOperator operator;
    private String value;
    private ColumnType columnType;
}

