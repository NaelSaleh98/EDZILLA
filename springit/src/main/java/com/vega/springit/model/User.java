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
    @NotEmpty(message = "Please, Enter your Email.")
    @Size(min = 8, max = 64, message = "Please, Email size should be between 8 and 64.")//default message in arabic
    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @NonNull
    @NotEmpty(message = "Please, Enter Password.")
    @Size(min = 8, max = 128, message = "Please, Password size should be between 8 and 128.")
    @Column(length = 128)
    private String password;

    @NonNull
    @Column(nullable = false)
    private boolean enabled;

    // ON
    @Transient
    @NotEmpty(message = "You must enter Password Confirmation.")
    private String confirmPassword;

    private String activationCode;

/////more detail about user
    // ON
    @NonNull
    @NotEmpty(message = "Please, Enter your First Name.")
    @Size(max = 32, message = "Please, Your First Name cannot contain more than 32 letters.")
    @Column(length = 32)
    private String firstName;

    //ON
    @NonNull
    @NotEmpty(message = "Please, Enter your Last Name.")
    @Size(max = 32, message = "Please, Your Last Name cannot contain more than 32 letters.")//default message in arabic
    @Column(length = 32)
    private String lastName;

    //ON
    @Transient //fullName is derived Property,@Transient-> don't create column for fullName in DB
    @Setter(AccessLevel.NONE)//don't create setter for fullName
    private String fullName;

    //ON : must check if exist
    @NonNull
    @NotEmpty(message = "Please, Enter your Alias.")
    @Size(min = 4 , max = 64, message = "Please, Alias size should be between 4 and 64.")//default message in arabic
    @Column(nullable = false, unique = true , length = 64)
    private String alias;

    //ON
    public String getFullName(){
        return firstName + " " + lastName;
    }

    //<for vote>
    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Report> reports = new ArrayList<>();

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
