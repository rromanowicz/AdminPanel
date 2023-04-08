package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.SectionType;
import lombok.Data;

import java.util.List;

/**
 * The {@code Action} class represents actions to be added in DATA_GRID.
 *     <ul>
 *         <li>{@code name} - Name of the section</li>
 *         <li>{@code type} - Type of section. See definitions for Details.
 *             <pre>Values: REPORT, DATA_GRID</pre></li>
 *         <li>{@code name} - Name of the section</li>
 *         <li>{@code type} - Type of section. See definitions for Details.
 *             <pre>Values: REPORT, DATA_GRID</pre></li>
 *         <li>{@code query} - SQL query to be used as base for the section.</li>
 *         <li>{@code columns} - List of columns that are returned by the query.</li>
 *         <li>{@code report} - REPORT type specific attributes.</li>
 *         <li>{@code dataGrid} - DATA_GRID type specific attributes.</li>
 *         <li>{@code params} - List of parameters to be applied only on this section.</li>
 *     </ul>
 *
 * @author  rromanowicz
 * @see     SectionType
 * @see     Column
 * @see     Report
 * @see     DataGrid
 * @see     Param
 */
@Data
public class PageSection {
    private String name;
    private SectionType type;
    private String query;
    private List<Column> columns;
    private Report report;
    private DataGrid dataGrid;
    private List<Param> params;
}
