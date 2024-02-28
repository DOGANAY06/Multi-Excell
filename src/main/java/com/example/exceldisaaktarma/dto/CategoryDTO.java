package com.example.exceldisaaktarma.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private List<ProductDTO> products;
    private List<SubCategoryDTO> subcategories;
    private List<String> productNames;
    private List<String> subCategoryNames;
}
