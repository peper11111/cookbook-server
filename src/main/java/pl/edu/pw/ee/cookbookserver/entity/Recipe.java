package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@Table(name = "cb_recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime creationTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cuisine cuisine;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
    @ElementCollection
    @CollectionTable(name = "cb_recipe_ingredients")
    @Column(name = "ingredient")
    private Collection<String> ingredients;
    @ElementCollection
    @CollectionTable(name = "cb_recipe_steps")
    @Column(name = "step")
    private Collection<String> steps;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL)
    private Collection<Comment> comments;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> likes;

    public Recipe() {
        this.creationTime = LocalDateTime.now();
    }
}
