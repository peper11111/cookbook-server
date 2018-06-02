package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cb_upload")
public class Upload {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String filename;
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
    private LocalDateTime creationTime;
}
