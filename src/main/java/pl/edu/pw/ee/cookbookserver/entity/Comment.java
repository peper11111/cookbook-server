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
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Collection<Comment> comments;

    public Comment() {
        this.creationTime = LocalDateTime.now();
    }
}
