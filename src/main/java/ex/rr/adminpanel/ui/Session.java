package ex.rr.adminpanel.ui;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import ex.rr.adminpanel.data.database.config.DataSourceRouting;
import ex.rr.adminpanel.data.enums.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.UUID;

@SpringComponent
@VaadinSessionScope
public class Session {

    private Env env;

    private final String sessionId;
    private final String username;

    private final DataSourceRouting dataSource;


    public Session(String username, ApplicationContext context) {
        this.sessionId = UUID.randomUUID().toString();
        this.username = username;
        this.env = Env.DEV; //Default env for session
        dataSource = dataSource(context);
        addMetadataToSession(context.getEnvironment());
    }

    private void addMetadataToSession(Environment environment) {
        VaadinSession.getCurrent().setAttribute("H2.tblQuery", environment.getProperty("spring.datasource.utils.H2.tblQuery"));
        VaadinSession.getCurrent().setAttribute("H2.colQuery", environment.getProperty("spring.datasource.utils.H2.colQuery"));
        VaadinSession.getCurrent().setAttribute("Oracle.tblQuery", environment.getProperty("spring.datasource.utils.Oracle.tblQuery"));
        VaadinSession.getCurrent().setAttribute("Oracle.tblQuery", environment.getProperty("spring.datasource.utils.Oracle.colQuery"));
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
        this.dataSource.setEnv(env);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @VaadinSessionScope
    @Primary
    @Autowired
    public DataSourceRouting dataSource(ApplicationContext context) {
        DataSourceRouting routingDataSource = new DataSourceRouting();
        routingDataSource.initDatasource(env,
                context.getBean("dsLocal", DataSource.class),
                context.getBean("dsDev", DataSource.class),
                context.getBean("dsSit", DataSource.class),
                context.getBean("dsSat", DataSource.class),
                context.getBean("dsProd", DataSource.class));
        return routingDataSource;
    }
}
