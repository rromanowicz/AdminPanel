package ex.rr.adminpanel.models.templates.page;

import lombok.Data;

import java.util.List;

@Data
public class InitialReport {
    private List<String> dimensions;
    private List<String> facts;
}
