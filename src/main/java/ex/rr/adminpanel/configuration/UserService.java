package ex.rr.adminpanel.configuration;

import ex.rr.adminpanel.database.User;
import ex.rr.adminpanel.database.UserRepository;
import ex.rr.adminpanel.enums.RoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsManager, AuthenticationManager {

    @Value("${app.secret.salt}")
    private String salt;

    private final UserRepository userRepository;

    @Transactional
    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void createUser(User user){
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    public void disable(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void createUser(UserDetails user) {
        createUser(new User(user.getUsername(), user.getPassword(), Set.of(RoleEnum.ROLE_USER), salt));
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save(new User(user.getUsername(), user.getPassword(), Set.of(RoleEnum.ROLE_USER), salt));
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username).orElseThrow();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> user = findByUsername(authentication.getName());
        if (user.isPresent() && user.get().checkPassword(authentication.getCredentials().toString(), salt)) {
            return new UsernamePasswordAuthenticationToken(user.get().getUsername(), user.get().getPassword(), user.get().getAuthorities());
        } else {
            return null;
        }
    }
}
