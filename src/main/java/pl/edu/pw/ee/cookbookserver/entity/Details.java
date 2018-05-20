package pl.edu.pw.ee.cookbookserver.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cb_details")
public class Details {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload avatar;
    @OneToOne(fetch = FetchType.LAZY)
    private Upload banner;
}
