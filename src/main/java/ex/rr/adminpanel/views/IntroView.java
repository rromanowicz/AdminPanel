package ex.rr.adminpanel.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import ex.rr.adminpanel.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Intro")
@AnonymousAllowed
public class IntroView extends VerticalLayout {
    IntroView() {
        add(new H1("Hello"));
    }
}
