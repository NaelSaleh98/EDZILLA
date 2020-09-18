package com.vega.springit.model;

import com.vega.springit.model.validator.PasswordsMatch;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;


@Getter @Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@PasswordsMatch
public class User implements UserDetails {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @Size(min = 8, max = 20, message = "Email size must be between 8 and 20")//default message in arabic
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(length = 100)
    @NotEmpty(message = "Please enter Password.")
    private String password;

    @NonNull
    @Column(nullable = false)
    private boolean enabled;

    // ON
    @Transient
    @NotEmpty(message = "Please enter Password Confirmation.")
    private String confirmPassword;

    private String activationCode;

/////more detail about user
    // ON
    @NonNull
    @NotEmpty(message = "You must enter First Name.")
    private String firstName;

    //ON
    @NonNull
    @NotEmpty(message = "You must enter Last Name.")
    private String lastName;

    //ON
    @Transient //fullName is derived Property,@Transient-> don't create column for fullName in DB
    @Setter(AccessLevel.NONE)//don't create setter for fullName
    private String fullName;

    //ON : must check if exist
    @NonNull
    @NotEmpty(message = "Please enter alias.")
    @Column(nullable = false, unique = true)
    private String alias;

    //ON
    public String getFullName(){
        return firstName + " " + lastName;
    }

    ////***************
    @ManyToMany(fetch = FetchType.EAGER)//(fetch = FetchType.EAGER) bring all roles , because number of roles is small
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }


    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    /*
    or:
        List<SimpleGrantedAuthority> a =new ArrayList<>();
        for(Role r : roles){
            a.add(new SimpleGrantedAuthority(r.getName()));
        }
        return a;
     */

    }
//****************
    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
