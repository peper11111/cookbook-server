package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "cb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Collection<Role> authorities;
    private String name;
    private String biography;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload avatar;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Collection<User> followers;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followers")
    private Collection<User> followed;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<Upload> images;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Collection<Recipe> recipes;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favourites")
    private Collection<Recipe> favouriteRecipes;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likes")
    private Collection<Recipe> likedRecipes;

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = false;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
