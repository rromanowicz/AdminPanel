package ex.rr.adminpanel.configuration;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import ex.rr.adminpanel.views.LoginView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.Set;

@Configuration
class SecurityConfiguration extends VaadinWebSecurity {

    @Value("${spring.h2.console.path}")
    private String h2ConsolePath;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
//                .requestMatchers("/", "").anonymous()
                .requestMatchers(h2ConsolePath).permitAll()
                ;
        super.configure(http);
        setLoginView(http, LoginView.class);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    UserDetailsManager userDetailsManager() {
        var users = List.of(
                User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build(),
                User.withDefaultPasswordEncoder().username("reports").password("password").roles("USER", "REPORTS").build(),
                User.withDefaultPasswordEncoder().username("admin").password("password").roles("USER", "ADMIN").build()
        );
        return new InMemoryUserDetailsManager(users);
    }
}
