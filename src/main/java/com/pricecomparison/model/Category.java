package com.pricecomparison.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Category {
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    private Long id;
    @NotBlank
    @Size(
            min = 3,
            message = "Title must contains at least 3 characters."
    )
    private String title;
    //TODO: co z imageURL?
    //TODO: zostawic?
    //private Boolean enabled;
    @ManyToOne
    @JoinColumn(
            name = "parent_id"
    )
    private Category parent;
    public Category(
            Long id,
            String title,
            Category parent
    ) {
        this.id = id;
        this.title = title;
        this.parent = parent;
    }
    public Category(
            String title,
            Category parent
    ) {
        this.title = title;
        this.parent = parent;
    }
}
