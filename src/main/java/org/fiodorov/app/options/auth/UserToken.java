package org.fiodorov.app.options.auth;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.jsonwebtoken.Claims;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class UserToken implements UserDetails, Serializable {

    private String username;

    private String password;

    private Long id;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean accountEnabled;

    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * Default Constructor
     */
    public UserToken() {

    }

    public UserToken(Claims claims) {
        username = claims.get("username").toString();

        if (claims.get("roles") instanceof List) {
            setRoles(((List<String>) claims.get("roles")).stream().map(UserRole::valueOf).collect(Collectors.toSet()));
        }

        if (claims.get("id") != null) {
            id = Long.valueOf(claims.get("id").toString());
        }
    }

    public UserToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Composite constructor
     *
     * @return
     */
    public UserToken(String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<SimpleGrantedAuthority> authorities) {
        if (username != null && !"".equals(username) && password != null) {
            this.username = username;
            this.password = password;
            this.accountEnabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = authorities;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public UserToken(String username, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<SimpleGrantedAuthority> authorities) {
        if (username != null && !"".equals(username)) {
            this.username = username;
            this.accountEnabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = authorities;
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public Set<UserRole> getRoles() {
        Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
        if (authorities != null) {
            roles.addAll(authorities.stream().map(UserRole::valueOf).collect(Collectors.toList()));
        }
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        roles.forEach(this::grantRole);
    }

    public void grantRole(UserRole role) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        authorities.add(role.asAuthorityFor());
    }

    public void revokeRole(UserRole role) {
        if (authorities != null) {
            authorities.remove(role.asAuthorityFor());
        }
    }

    public boolean hasRole(UserRole role) {
        return authorities.contains(role.asAuthorityFor());
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return accountEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserToken token = (UserToken) o;
        return accountNonExpired == token.accountNonExpired &&
                accountNonLocked == token.accountNonLocked &&
                credentialsNonExpired == token.credentialsNonExpired &&
                accountEnabled == token.accountEnabled &&
                Objects.equals(username, token.username) &&
                Objects.equals(password, token.password) &&
                Objects.equals(authorities, token.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, accountNonExpired, accountNonLocked, credentialsNonExpired,
                accountEnabled, authorities);
    }
}
