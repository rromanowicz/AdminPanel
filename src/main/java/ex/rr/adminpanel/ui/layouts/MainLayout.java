package ex.rr.adminpanel.ui.layouts;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import ex.rr.adminpanel.database.config.EnvContextHolder;
import ex.rr.adminpanel.enums.Env;
import ex.rr.adminpanel.enums.RoleEnum;
import ex.rr.adminpanel.ui.views.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;


public class MainLayout extends AppLayout {

    private HorizontalLayout header;
    private HorizontalLayout opts;


    protected void onAttach(AttachEvent attachEvent) {
        UI ui = getUI().get();
        Button button;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal)) {
            button = new Button("Login", event -> ui.navigate(LoginView.class));
        } else {
            button = new Button("Logout", event -> {
                SecurityContextHolder.clearContext();
                ui.getSession().close();
                UI.getCurrent().getPage().setLocation("/logout");
            });

            Label username = new Label(StringUtils.capitalize(SecurityContextHolder.getContext().getAuthentication().getName()));
            opts.add(username);

            ui.setPollInterval(3000);
        }

        opts.add(button);

        var themeToggle = new Checkbox("Dark");
        themeToggle.addValueChangeListener(e -> setTheme(e.getValue()));
        themeToggle.setValue(true);
        opts.add(themeToggle);
    }

    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";
        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Admin Panel");
        logo.addClassNames("text-l", "m-m");

        opts = new HorizontalLayout();
        opts.setPadding(true);
        opts.setAlignItems(FlexComponent.Alignment.CENTER);
        opts.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout layout = new HorizontalLayout(new DrawerToggle(), logo);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        header = new HorizontalLayout(layout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        ComboBox<Env> env = new ComboBox<>();
        env.setItems(Env.values());
        env.setValue(EnvContextHolder.getEnvContext());
        env.addValueChangeListener(event -> EnvContextHolder.setEnvContext(env.getValue()));

        Button reload = new Button(new Icon(VaadinIcon.REFRESH));
        reload.addThemeVariants(ButtonVariant.LUMO_ICON);
        reload.getElement().setAttribute("aria-label", "Reload");
        reload.addClickListener(e -> UI.getCurrent().getPage().reload());

        header.add(new Div(env, reload));
        header.add(opts);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        addToNavbar(header);
    }

    private void createDrawer() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        tabs.add(spacerTab("General"));
        tabs.add(createTab(VaadinIcon.HOME, "Home", IntroView.class));
        tabs.add(createTab(VaadinIcon.CHAT, "Chat", ChatView.class));


        if (hasAnyRole(RoleEnum.ROLE_REPORTS, RoleEnum.ROLE_ADMIN)) {
            tabs.add(spacerTab("Reports"));
            tabs.add(createTab(VaadinIcon.TABLE, "Report", ReportView.class));
            tabs.add(createTab(VaadinIcon.TABLE, "Query Report", QueryReportView.class));
            tabs.add(createTab(VaadinIcon.TABLE, "Table Report", TableReportView.class));
        }

        if (hasAnyRole(RoleEnum.ROLE_ADMIN)) {
            tabs.add(spacerTab("Admin"));
            tabs.add(createTab(VaadinIcon.DATABASE, "Triggers", TriggersView.class));
            tabs.add(createTab(VaadinIcon.DATABASE, "Triggers V2", TriggersViewV2.class));
        }

        addToDrawer(tabs);
    }

    private static boolean hasAnyRole(RoleEnum... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Arrays.stream(roles).anyMatch(r -> authentication.getAuthorities().contains(r));

    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private Tab spacerTab(String name) {
        Tab tab = new Tab(name);
        tab.setEnabled(false);
        return tab;
    }


}