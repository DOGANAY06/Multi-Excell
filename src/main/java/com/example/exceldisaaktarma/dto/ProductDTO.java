package com.example.exceldisaaktarma.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String code;
    private BigDecimal price;
    private Long quantity;
    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;

    public ProductDTO(String name) {
        this.name=name;
    }

    public ProductDTO() {

    }
}
