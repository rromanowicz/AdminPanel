package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.SectionType;
import lombok.Data;

import java.util.List;

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
