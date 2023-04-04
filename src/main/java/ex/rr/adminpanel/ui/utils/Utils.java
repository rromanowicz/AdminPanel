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

public class Utils {

    public static void displayNotification(NotificationVariant theme, String... messageLines) {
        Notification notification = new Notification();
        notification.addThemeVariants(theme);

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

}
