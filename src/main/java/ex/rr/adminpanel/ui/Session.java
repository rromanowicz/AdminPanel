package ex.rr.adminpanel.ui;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import ex.rr.adminpanel.database.config.DataSourceRouting;
import ex.rr.adminpanel.enums.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.UUID;

@SpringComponent
@VaadinSessionScope
public class Session {

    public static final String SPRING_DATASOURCE_LOCAL = "spring.datasource.local.";
    public static final String SPRING_DATASOURCE_DEV = "spring.datasource.dev.";
    public static final String SPRING_DATASOURCE_SIT = "spring.datasource.sit.";
    public static final String SPRING_DATASOURCE_SAT = "spring.datasource.sit.";
    public static final String SPRING_DATASOURCE_PROD = "spring.datasource.prod.";

    private final Environment environment;

    private Env env;

    private final String sessionId;
    private final String username;
    private final DataSourceRouting routingDataSource;

    private final DataSource dataSource;

    public Session(String username, Environment environment) {
        this.sessionId = UUID.randomUUID().toString();
        this.username = username;
        this.environment = environment;
        routingDataSource = new DataSourceRouting();
        dataSource = dataSource();
    }

    public String getSessionId() {
        return sessionId;
    }


    public String getUsername() {
        return username;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @VaadinSessionScope
    @Primary
    @Autowired
    public DataSource dataSource() {
//        DataSourceRouting routingDataSource = new DataSourceRouting();
        routingDataSource.initDatasource(localDataSource(), devDataSource(), sitDataSource(), satDataSource(), prodDataSource());
        return routingDataSource;
    }

    @VaadinSessionScope
    public DataSource localDataSource() {
        return DataSourceBuilder.create().url(environment.getProperty(SPRING_DATASOURCE_LOCAL + "jdbcUrl"))
                .driverClassName(environment.getProperty(SPRING_DATASOURCE_LOCAL + "driverClassName"))
                .username(environment.getProperty(SPRING_DATASOURCE_LOCAL + "username"))
                .password(environment.getProperty(SPRING_DATASOURCE_LOCAL + "password"))
                .build();
    }

    @VaadinSessionScope
    public DataSource devDataSource() {
        return DataSourceBuilder.create().url(environment.getProperty(SPRING_DATASOURCE_DEV + "jdbcUrl"))
                .driverClassName(environment.getProperty(SPRING_DATASOURCE_DEV + "driverClassName"))
                .username(environment.getProperty(SPRING_DATASOURCE_DEV + "username"))
                .password(environment.getProperty(SPRING_DATASOURCE_DEV + "password"))
                .build();
    }

    @VaadinSessionScope
    public DataSource sitDataSource() {
        return DataSourceBuilder.create().url(environment.getProperty(SPRING_DATASOURCE_SIT + "jdbcUrl"))
                .driverClassName(environment.getProperty(SPRING_DATASOURCE_SIT + "driverClassName"))
                .username(environment.getProperty(SPRING_DATASOURCE_SIT + "username"))
                .password(environment.getProperty(SPRING_DATASOURCE_SIT + "password"))
                .build();
    }

    @VaadinSessionScope
    public DataSource satDataSource() {
        return DataSourceBuilder.create().url(environment.getProperty(SPRING_DATASOURCE_SAT + "jdbcUrl"))
                .driverClassName(environment.getProperty(SPRING_DATASOURCE_SAT + "driverClassName"))
                .username(environment.getProperty(SPRING_DATASOURCE_SAT + "username"))
                .password(environment.getProperty(SPRING_DATASOURCE_SAT + "password"))
                .build();
    }

    @VaadinSessionScope
    public DataSource prodDataSource() {
        return DataSourceBuilder.create().url(environment.getProperty(SPRING_DATASOURCE_PROD + "jdbcUrl"))
                .driverClassName(environment.getProperty(SPRING_DATASOURCE_PROD + "driverClassName"))
                .username(environment.getProperty(SPRING_DATASOURCE_PROD + "username"))
                .password(environment.getProperty(SPRING_DATASOURCE_PROD + "password"))
                .build();
    }
}
