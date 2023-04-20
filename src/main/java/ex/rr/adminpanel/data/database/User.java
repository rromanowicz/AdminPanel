package ex.rr.adminpanel.data.database;

import ex.rr.adminpanel.data.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "T_USER")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String username;
    @Email
    private String email;
    private String password;

    @ElementCollection
    @CollectionTable(name = "T_ROLES", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "roles")
    @Fetch(FetchMode.JOIN)
    private Set<RoleEnum> roles;
    private boolean active;

    public User() {
    }

    public User(String username, String password, Set<RoleEnum> roles, String salt) {
        this.username = username;
        this.roles = roles;
        this.password = encode(password, salt);
        this.active = false;
    }

    public boolean checkPassword(String password, String salt) {
        return encode(password, salt).equals(this.password);
    }

    public void updatePassword(String password, String salt) {
        this.password = encode(password, salt);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    private String encode(String password, String salt) {
        return DigestUtils.sha1Hex(password + salt);
    }

}
