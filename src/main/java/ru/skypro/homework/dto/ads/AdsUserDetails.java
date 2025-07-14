package ru.skypro.homework.dto.ads;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.Users;

import java.util.Collection;
import java.util.Collections;

import static ru.skypro.homework.dto.Role.ADMIN;
import static ru.skypro.homework.dto.Role.USER;

@Getter
public class AdsUserDetails implements UserDetails {
    private final Integer id;
    private final String username;
    private final String password;
    private final boolean isAdmin;

    public AdsUserDetails(Integer id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                isAdmin
                        ? new SimpleGrantedAuthority(ADMIN.toString())
                        : new SimpleGrantedAuthority(USER.toString());
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AdsUserDetails from(Users user) {
        boolean isAdminForUser = user.getRole() == ADMIN;
        return new AdsUserDetails(user.getId(), user.getUsername(), user.getPassword(), isAdminForUser);
    }
}
