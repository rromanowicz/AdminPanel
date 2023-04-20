package ex.rr.adminpanel.data.models.templates.page;

import lombok.Data;

/**
 * The {@code Param} class represents parameter to be applied only on the section.
 * <ul>
 *     <li>{@code name} - TBD</li>
 *     <li>{@code value} - TBD</li>
 * </ul>
 *
 * @author  rromanowicz
 */
@Data
public class Param {
    private String name; //TODO Column?
    private String value;
}
