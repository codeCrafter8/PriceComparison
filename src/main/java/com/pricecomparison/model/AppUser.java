package com.pricecomparison.model;

import com.pricecomparison.enumeration.AppUserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Column(
            unique = true
    )
    @NotBlank
    private String username;
    @Column(
            unique = true
    )
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Column(
            unique = true
    )
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts;
    private boolean locked;
    private boolean enabled;
    public AppUser(Long id,
                   String firstName,
                   String lastName,
                   String username,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }
    public AppUser(String firstName,
                   String lastName,
                   String username,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
