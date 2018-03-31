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
    @ManyToOne
    private User user;
    private UUID uuid;
    private LocalDateTime expirationTime;
}
