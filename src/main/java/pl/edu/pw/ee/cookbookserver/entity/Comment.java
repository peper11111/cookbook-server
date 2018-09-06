package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "cb_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    private String content;
    private LocalDateTime creationTime;
    @OneToMany(fetch = FetchType.LAZY)
    private Collection<Comment> comments;

    public Comment() {
        this.creationTime = LocalDateTime.now();
    }
}
