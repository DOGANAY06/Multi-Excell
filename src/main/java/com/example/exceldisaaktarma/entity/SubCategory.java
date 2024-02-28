package com.example.exceldisaaktarma.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "subcategory")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcategoryId;

    private String subcategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subcategory")
    private Set<Product> products;


    @Override
    public int hashCode() {
        return Objects.hash(subcategoryId, subcategoryName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SubCategory subCategory = (SubCategory) obj;
        return Objects.equals(subcategoryId, subCategory.subcategoryId) &&
                Objects.equals(subcategoryName, subCategory.subcategoryName);
    }
}

