package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "cb_token")
public class Token {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(unique = true)
    private String uuid;
    private LocalDateTime expirationTime;

    public Token(User user) {
        this.user = user;
        this.uuid = UUID.randomUUID().toString();
        this.expirationTime = LocalDateTime.now().plusHours(1);
    }
}
