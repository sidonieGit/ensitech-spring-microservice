package com.ensitech.auth_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(unique = true, nullable = false)
    private String username; */
    // @NotBlank(message = "L'email  est obligatoire")
    @Column(unique = true, nullable = false)
    private String email;

    //@NotBlank(message = "Le mot de passe  est obligatoire")
    @Column(nullable = false)
    private String password;


    // @NotNull(message = "Le role est obligatoire")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // private boolean enabled = true;
    //@Temporal(TemporalType.TIMESTAMP)
    // @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();
    
    // private boolean enabled;
    private String tokenUser;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /*@Override
    public boolean isEnabled() { return this.enabled; }*/
}
