package ex.rr.adminpanel.datasource;

import javax.sql.DataSource;

import ex.rr.adminpanel.database.DbTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;


@Configuration
@EnableJpaRepositories(basePackages = "ex.rr.adminpanel.database")
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @Primary
    @Autowired
    public DataSource dataSource() {
        DataSourceRouting routingDataSource = new DataSourceRouting();
        routingDataSource.initDatasource(localDataSource(), devDataSource());
        return routingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.local.datasource")
    public DataSource localDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.dev.datasource")
    public DataSource devDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
//        return builder.dataSource(dataSource()).packages(DbTrigger.class).properties(Map.of("hibernate.hbm2ddl.auto", "update")).build();
//    }
//
//    @Bean(name = "transactionManager")
//    public JpaTransactionManager transactionManager(
//            @Autowired @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
//    }
}