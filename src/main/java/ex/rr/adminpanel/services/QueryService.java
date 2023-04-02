package ex.rr.adminpanel.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.grid.Grid;
import ex.rr.adminpanel.exceptions.UserInputException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class QueryService {

    @Autowired
    EntityManager entityManager;

    private List<Tuple> queryResults;

    public QueryService withQuery(String query) {
        validateQuery(query);
        this.queryResults = entityManager.createNativeQuery(query, Tuple.class).getResultList();
        return this;
    }

    public String toJson() {
        List<ObjectNode> json = new ArrayList<ObjectNode>();
        ObjectMapper mapper = new ObjectMapper();
        for (Tuple t : queryResults) {
            List<TupleElement<?>> cols = t.getElements();
            ObjectNode one = mapper.createObjectNode();
            for (TupleElement col : cols) {
                one.put(col.getAlias(), t.get(col.getAlias()).toString());
            }
            json.add(one);
        }
        return json.toString();
    }

    public Grid<Tuple> toGrid(Grid.SelectionMode selectionMode) {
        Grid<Tuple> grid = new Grid<>(Tuple.class, false);

        if (selectionMode != null) {
            grid.setSelectionMode(selectionMode);
        }

        queryResults.get(0).getElements().forEach(col -> grid.addColumn(c -> c.get(col.getAlias())).setHeader(col.getAlias()));
        grid.setItems(queryResults);
        return grid;
    }

    private void validateQuery(String query) {
        Set<String> blockList = Set.of("INSERT", "ALTER", "DROP", "UPDATE", "CREATE", "--");
        String s = query.toUpperCase();

        List<String> violations = blockList.stream().filter(s::contains).toList();
        if (!violations.isEmpty()) {
            throw new UserInputException(String.format("Unexpected %s in 'SELECT' query.", violations));
        }
    }
}
