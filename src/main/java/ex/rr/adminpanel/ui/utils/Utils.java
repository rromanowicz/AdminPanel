package ex.rr.adminpanel.ui.utils;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import ex.rr.adminpanel.data.enums.Env;
import ex.rr.adminpanel.ui.Session;
import org.springframework.context.ApplicationContext;

/**
 * Utility class.
 *
 * @author rromanowicz
 */
public class Utils {

    /**
     * Displays a popup notification with 5sec timeout.
     *
     * @param theme        Notification theme.
     * @param messageLines Each entry is displayed in separate line.
     */
    public static void displayNotification(NotificationVariant theme, String... messageLines) {
        Notification notification = new Notification();
        notification.addThemeVariants(theme);
        notification.setDuration(5000);

        VerticalLayout vLayout = new VerticalLayout();
        for (String s : messageLines) {
            vLayout.add(new Div(new Text(s)));
        }

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout hLayout = new HorizontalLayout(vLayout, closeButton);
        hLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(hLayout);
        notification.open();
    }

    /**
     * Returns current user session.
     *
     * @return session
     * @see Session
     */
    public static Session getUserSession() {
        VaadinSession session = VaadinSession.getCurrent();
        try {
            return (Session) session.getAttribute("userSession");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Clears user session from Vaadin context.
     */
    public static void clearSession() {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("userSession", null);
    }

    /**
     * Creates Session bean and stores in Vaadin context.
     *
     * @param username User login.
     * @param context  Application properties environment.
     * @see Session
     */
    public static void createSession(String username, ApplicationContext context) {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("userSession", new Session(username, context, Env.DEV));
    }
}
