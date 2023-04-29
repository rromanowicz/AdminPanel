package ex.rr.adminpanel.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class Database {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsLocal")
    @ConfigurationProperties("spring.datasource.local")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsDev")
    @ConfigurationProperties("spring.datasource.dev")
    public DataSource devDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsSit")
    @ConfigurationProperties("spring.datasource.sit")
    public DataSource sitDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsSat")
    @ConfigurationProperties("spring.datasource.sat")
    public DataSource satDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsProd")
    @ConfigurationProperties("spring.datasource.prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create().build();
    }
}
