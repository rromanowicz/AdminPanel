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

/**
 * The {@code UserService} service class for user management and authentication.
 *
 * @author rromanowicz
 * @see UserDetails
 * @see User
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsManager, AuthenticationManager {

    @Value("${app.secret.salt}")
    private String salt;

    private final UserRepository userRepository;

    /**
     * Fetch user by username.
     *
     * @param username user login
     * @return User
     * @see User
     */
    @Transactional
    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * Creates new user with {@link User} entity.
     *
     * @param user {@link User}
     */
    public void createUser(User user) {
        userRepository.save(user);
    }

    /**
     * Returns all users from database.
     *
     * @return List<User>
     * @see User
     */
    public List<User> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    /**
     * Disable user by id.
     *
     * @param id User id.
     */
    public void disable(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Creates new user with {@link UserDetails}
     *
     * @param user {@link UserDetails}
     */
    @Override
    public void createUser(UserDetails user) {
        createUser(new User(user.getUsername(), user.getPassword(), Set.of(RoleEnum.ROLE_USER), salt));
    }

    /**
     * Updates user data.
     *
     * @param user {@link UserDetails}
     */
    @Override
    public void updateUser(UserDetails user) {
        User dbUser = findByUsername(user.getUsername()).orElseThrow();
        if (!user.getPassword().equals(dbUser.getPassword()) && !dbUser.checkPassword(user.getPassword(), salt)) {
            dbUser.updatePassword(user.getPassword(), salt);
        }
        dbUser.setActive(((User) user).isActive());
        dbUser.setRoles(((User) user).getRoles());
        dbUser.setEmail(((User) user).getEmail());

        userRepository.save(dbUser);
    }

    /**
     * Deletes user by username.
     *
     * @param username Username.
     */
    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    /**
     * @param oldPassword current password (for re-authentication if required)
     * @param newPassword the password to change to
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    /**
     * Checks if user with given name exists in database.
     *
     * @param username Username.
     * @return boolean
     */
    @Override
    public boolean userExists(String username) {
        return findByUsername(username).isPresent();
    }

    /**
     * @param username the username identifying the user whose data is required.
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username).orElseThrow();
    }

    /**
     * Verifies users authentication details.
     *
     * @param authentication the authentication request object
     * @return Authentication
     * @throws AuthenticationException
     */
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
