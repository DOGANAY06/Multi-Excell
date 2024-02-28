package com.example.exceldisaaktarma.service.impl;


import com.example.exceldisaaktarma.dto.CategoryDTO;
import com.example.exceldisaaktarma.dto.SubCategoryDTO;
import com.example.exceldisaaktarma.entity.Category;
import com.example.exceldisaaktarma.entity.Product;
import com.example.exceldisaaktarma.entity.SubCategory;
import com.example.exceldisaaktarma.repository.SubCategoryRepository;
import com.example.exceldisaaktarma.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;


    @Override
    public List<SubCategoryDTO> getAllSubCategory() {
        List<Object[]> subCategories = subCategoryRepository.findSubCategoryDetails();
        return subCategories.stream().map(subCategoryData -> {
            SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
            subCategoryDTO.setSubcategoryName((String) subCategoryData[0]); // subcategoryName
            subCategoryDTO.setProductNames(Collections.singletonList((String) subCategoryData[1])); // product name
            subCategoryDTO.setCategoryNames(Collections.singletonList((String) subCategoryData[2])); // category name
            return subCategoryDTO;
        }).collect(Collectors.toList());
    }

}
