package ex.rr.adminpanel.data.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_REPORTS;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
