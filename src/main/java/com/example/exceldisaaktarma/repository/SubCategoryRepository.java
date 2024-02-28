package com.example.exceldisaaktarma.repository;

import com.example.exceldisaaktarma.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {

    @Query("SELECT s.subcategoryName, p.name, c.categoryName " +
            "FROM SubCategory s " +
            "JOIN s.products p " +
            "JOIN s.category c")
    List<Object[]> findSubCategoryDetails();

}
