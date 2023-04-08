package ex.rr.adminpanel.database.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * The {@code DataSourceConfig} configuration class initializes all the data sources used by the Router.
 * <p>All defined sources will share the same Entity classes. Only one will be used at any given time.</p>
 * <p>Definitions are sourced from the property file.</p>
 *
 * @author Robert Romanowicz
 * @see DataSourceRouting
 */
@Configuration
@EnableJpaRepositories(basePackages = "ex.rr.adminpanel.database")
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @Primary
    @Autowired
    public DataSource dataSource() {
        DataSourceRouting routingDataSource = new DataSourceRouting();
        routingDataSource.initDatasource(localDataSource(), devDataSource(), sitDataSource(), satDataSource(), prodDataSource());
        return routingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dev")
    public DataSource devDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.sit")
    public DataSource sitDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.sat")
    public DataSource satDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create().build();
    }

}