package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_cuisine")
public class Cuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Cuisine() {
        this(null);
    }

    public Cuisine(String name) {
        this.name = name;
    }
}
