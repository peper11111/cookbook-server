package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;
import pl.edu.pw.ee.cookbookserver.misc.TokenType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "cb_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Enumerated
    private TokenType type;
    @Column(unique = true)
    private String uuid;
    private LocalDateTime expirationTime;

    public Token() {
        this(null, null);
    }

    public Token(User user, TokenType type) {
        this.user = user;
        this.type = type;
        this.uuid = UUID.randomUUID().toString();
        this.expirationTime = LocalDateTime.now().plusDays(1);
    }
}
