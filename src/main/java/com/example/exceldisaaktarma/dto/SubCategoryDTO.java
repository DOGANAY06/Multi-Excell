package com.example.exceldisaaktarma.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubCategoryDTO {
    private Long subcategoryId;
    private String subcategoryName;
    private List<ProductDTO> products;
    private List<CategoryDTO> categorys;
    private List<String> productNames;
    private List<String> categoryNames;

}
