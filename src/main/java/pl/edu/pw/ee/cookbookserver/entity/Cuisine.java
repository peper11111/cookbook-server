package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_cuisine")
public class Cuisine {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
}
