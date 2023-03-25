package ex.rr.adminpanel.layouts;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ex.rr.adminpanel.configuration.RoleEnum;
import ex.rr.adminpanel.views.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;


public class MainLayout extends AppLayout {


    protected void onAttach(AttachEvent attachEvent) {
        UI ui = getUI().get();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal)) {
            Button loginButton = new Button("Login", event -> ui.navigate(LoginView.class));
            addToNavbar(loginButton);
        } else {

            Button button = new Button("Logout", event -> {
                SecurityContextHolder.clearContext();
                ui.getSession().close();
                UI.getCurrent().getPage().setLocation("/logout");
            });

            Label username = new Label(SecurityContextHolder.getContext().getAuthentication().getName());

            addToNavbar(username);
            addToNavbar(button);

            ui.setPollInterval(3000);
        }
    }

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Admin Panel");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

//        Button logout = new Button("Logout", e -> {
//            SecurityContextHolder.clearContext();
//            e.getSource().getUI().getSession().close();
//            Page.setLocation(...);
//        });

        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(createLink(VaadinIcon.HOME, "Home", IntroView.class));
        verticalLayout.add(createLink(VaadinIcon.CHAT, "Chat", ChatView.class));


        if (hasAnyRole(RoleEnum.ROLE_REPORTS, RoleEnum.ROLE_ADMIN)) {
            verticalLayout.add(createLink(VaadinIcon.TABLE, "Report", ReportView.class));
        }

        if (hasAnyRole(RoleEnum.ROLE_ADMIN)) {
            verticalLayout.add(
                    createLink(VaadinIcon.DATABASE, "DB Triggers", DbTriggersView.class),
                    createLink(VaadinIcon.DATABASE, "DB Triggers V2", DbTriggersViewV2.class)
            );
        }

        addToDrawer(verticalLayout);
    }

    private static boolean hasAnyRole(RoleEnum... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Arrays.stream(roles).anyMatch(r ->
                authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().contains(r.name())));

    }

    private RouterLink createLink(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return link;
    }
}