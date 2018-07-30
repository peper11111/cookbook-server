package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cb_recipe")
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
    private String title;
    private String lead;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cuisine cuisine;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
}