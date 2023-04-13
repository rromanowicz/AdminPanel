package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Route("login")
public
class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    Environment env;


    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.addLoginListener(event -> Utils.createSession(event.getUsername(), env));

        add(new H1("Welcome"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}