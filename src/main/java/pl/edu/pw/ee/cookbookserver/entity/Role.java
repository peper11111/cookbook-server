package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String authority;

    public Role() {
        this(null);
    }

    public Role(String authority) {
        this.authority = authority;
    }
}
