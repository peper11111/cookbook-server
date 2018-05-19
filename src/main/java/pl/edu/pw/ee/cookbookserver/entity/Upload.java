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
    private String filename;
    @ManyToOne
    private User owner;
    private LocalDateTime creationTime;
}
