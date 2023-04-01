package ex.rr.adminpanel;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Push
public class AdminPanelApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(AdminPanelApplication.class, args);
    }

}


