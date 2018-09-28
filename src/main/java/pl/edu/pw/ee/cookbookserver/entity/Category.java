package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Category() {
        this(null);
    }

    public Category(String name) {
        this.name = name;
    }
}
