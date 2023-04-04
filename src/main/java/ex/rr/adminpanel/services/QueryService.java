package ex.rr.adminpanel.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import ex.rr.adminpanel.exceptions.UserInputException;
import ex.rr.adminpanel.models.templates.page.Action;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class QueryService {

    @Autowired
    EntityManager entityManager;

    private List<Tuple> queryResults;
    private List<Action> actions;

    public QueryService withQuery(String query) {
        validateQuery(query);
        this.queryResults = entityManager.createNativeQuery(query, Tuple.class).getResultList();
        this.actions = null;
        return this;
    }

    public QueryService withActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public String toJson() {
        if (queryResults.isEmpty())
            return null;

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

    public List<Map<String, String>> toMap() {
        if (queryResults.isEmpty())
            return null;

        List<Map<String, String>> list = new ArrayList<>();
        for (Tuple t : queryResults) {
            List<TupleElement<?>> cols = t.getElements();
            Map<String, String> map = new HashMap<>();
            for (TupleElement col : cols) {
                map.put(col.getAlias(), t.get(col.getAlias()).toString());
            }
            list.add(map);
        }
        return list;
    }

    public Grid<Tuple> toGrid(Grid.SelectionMode selectionMode) {
        Grid<Tuple> grid = new Grid<>(Tuple.class, false);
        grid.setSelectionMode(selectionMode);

        queryResults.get(0).getElements().forEach(col -> grid.addColumn(c -> c.get(col.getAlias())).setHeader(col.getAlias()));

        if (actions != null && !actions.isEmpty()) {
            actions.forEach(action -> grid.addComponentColumn(c -> {
                Button editButton = new Button(action.getName());
                editButton.addClickListener(e -> executeAction(action, c));
                return editButton;
            }).setWidth("150px").setFlexGrow(0));
        }

        grid.setItems(queryResults);
        return grid;
    }

    private void executeAction(Action action, Tuple c) {
        String url = prepareString(action, c);
        switch (action.getAction()) {
            case LOG -> log.info(url);
            case REST -> log.info("REST Call: [{} {}]", action.getType(), url);
            case TEAMS_MESSAGE -> log.info("TEAMS_MESSAGE Publishing: [{}]", url);
        }
    }

    private String prepareString(Action action, Tuple c) {
        String str = action.getUrl();

        for (Map.Entry<String, String> entry : action.getData()) {
            str = str.replace(String.format("${%s}", entry.getKey()), c.get(entry.getValue(), String.class));
        }

        return str;
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


@Data
@Builder
class Wrapper {
    List<Map<String, String>> map;
}