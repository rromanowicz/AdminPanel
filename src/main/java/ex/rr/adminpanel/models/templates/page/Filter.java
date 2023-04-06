package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.ColumnType;
import ex.rr.adminpanel.enums.FilterOperator;
import lombok.Data;


/**
 * The {@code Filter} class represents global filter section.<p>
 * {@code column} - name of the column to be used<p>
 * {@code operator} - logical operator
 * <pre>
 *       Values: EQUAL, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL
 *  </pre><p>
 * {@code value} - value to be used in filter (for DATE and DATETIME, a relative value TODAY{+/-x} can be used). For example:
 * <pre>
 *       String value = "2023-04-06"
 *       String value = "TODAY"
 *       String value = "TODAY-7"
 * </pre><p>
 * {@code columnType} - type of the column
 * <pre>
 *       Values: TEXT, NUMBER, DATE, DATETIME
 * </pre>
 *
 * @author Robert Romanowicz
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

