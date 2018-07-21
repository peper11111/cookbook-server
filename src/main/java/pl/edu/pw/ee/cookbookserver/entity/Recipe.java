package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_recipe")
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cuisine cuisine;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
}
