package com.nkd.eida_backend.Domain;

import com.nkd.eida_backend.Entity.CredentialEntity;
import com.nkd.eida_backend.Entity.UserEntity;
import com.nkd.eida_backend.Enumeration.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter @Setter
@Builder
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private UserEntity user;
    private CredentialEntity credential;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return credential.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credential.getIsCredentialNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEnabled();
    }
}
