package com.example.exceldisaaktarma.service.impl;

import com.example.exceldisaaktarma.dto.CategoryDTO;
import com.example.exceldisaaktarma.dto.ProductDTO;
import com.example.exceldisaaktarma.dto.SubCategoryDTO;
import com.example.exceldisaaktarma.entity.Category;
import com.example.exceldisaaktarma.entity.Product;
import com.example.exceldisaaktarma.entity.SubCategory;
import com.example.exceldisaaktarma.repository.CategoryRepository;
import com.example.exceldisaaktarma.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAllWithProductsAndSubcategories();
        return categories.stream().map(category -> {
            CategoryDTO categoryDTO = convertToDTO(category);
            categoryDTO.setProductNames(category.getProducts().stream().map(Product::getName).collect(Collectors.toList()));
            categoryDTO.setSubCategoryNames(category.getSubcategories().stream().map(SubCategory::getSubcategoryName).collect(Collectors.toList()));
            return categoryDTO;
        }).collect(Collectors.toList());
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());



        return categoryDTO;
    }

//    private SubCategoryDTO convertSubCategoryToDTO(SubCategory subCategory) {
//        SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
//        subCategoryDTO.setSubcategoryName(subCategory.getSubcategoryName());
//        return subCategoryDTO;
//    }
//
//    private ProductDTO convertProductToDTO(Product product) {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setName(product.getName());
//        return productDTO;
//    }

}
