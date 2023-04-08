package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

/**
 * The {@code InitialReport} class represents the initial report configuration.<p>
 * {@code dimensions} - List of column names that will be used as reports dimensions<p>
 * {@code facts} - List of column names that will be used as reports fact values<p>
 *
 * @author  rromanowicz
 */
@Data
public class InitialReport {
    private List<String> dimensions;
    private List<String> facts;
}
