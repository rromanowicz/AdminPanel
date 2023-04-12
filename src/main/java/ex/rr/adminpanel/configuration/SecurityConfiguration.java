package ex.rr.adminpanel.configuration;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import ex.rr.adminpanel.ui.views.LoginView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
class SecurityConfiguration extends VaadinWebSecurity {

    private final DataSource localDataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/", "").permitAll();
        super.configure(http);
        setLoginView(http, LoginView.class);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(localDataSource);

        return jdbcUserDetailsManager;
    }


    @Bean
    UserDetailsManager userDetailsManager() {
        return jdbcUserDetailsManager();
    }
}
