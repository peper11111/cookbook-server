package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "cb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> authorities;
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload avatar;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
}
