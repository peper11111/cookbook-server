package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "cb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> authorities;
    private String name;
    private String biography;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload avatar;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
    @OneToMany(mappedBy = "author")
    private Collection<Recipe> recipes;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<User> followed;
    @ManyToMany(mappedBy = "followed")
    private Collection<User> followers;

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = false;
    }

    public void setUsername(String username) throws ProcessingException {
        if (username == null) {
            throw new ProcessingException(Error.NULL_USERNAME);
        }
        this.username = username;
    }

    public void setEmail(String email) throws ProcessingException {
        if (email == null) {
            throw new ProcessingException(Error.NULL_EMAIL);
        }
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new ProcessingException(Error.INVALID_EMAIL);
        }
        this.email = email;
    }

    public void setPassword(String password) throws ProcessingException {
        if (password == null) {
            throw new ProcessingException(Error.NULL_PASSWORD);
        }
        if (password.length() < 8) {
            throw new ProcessingException(Error.PASSWORD_TOO_SHORT);
        }
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
