package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

/**
 * The {@code Report} class represents the displayed report configuration.
 * <ul>
 *     <li>{@code dimensions} - List of column names that will be used as reports dimensions</li>
 *     <li>{@code facts} - List of column names that will be used as reports fact values</li>
 *     <li>{@code initialReport} - Initial view when report is loaded</li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     InitialReport
 */
@Data
public class Report {
    private List<String> dimensions;
    private List<String> facts;
    private InitialReport initialReport;
}
