package ex.rr.adminpanel.models.templates.page;

import ex.rr.adminpanel.enums.ColumnType;
import lombok.Data;

@Data
public class Column {
    private String name;
    private ColumnType type;
}
