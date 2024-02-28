package com.example.exceldisaaktarma.service.impl;

import com.example.exceldisaaktarma.dto.ExcelMetaDataDTO;
import com.example.exceldisaaktarma.dto.ProductDTO;
import com.example.exceldisaaktarma.dto.ResourceDTO;
import com.example.exceldisaaktarma.entity.Category;
import com.example.exceldisaaktarma.entity.Product;
import com.example.exceldisaaktarma.entity.SubCategory;
import com.example.exceldisaaktarma.repository.ProductRepository;
import com.example.exceldisaaktarma.service.ExcelService;
import com.example.exceldisaaktarma.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ExcelService excelService;
    private final ProductRepository productRepository;

    @Override
    public Product create(ProductDTO productDTO) {
        final var product = new Product();
        product.setCode(productDTO.getCode());
        product.setPrice(productDTO.getPrice());
        product.setName(productDTO.getName());
        product.setQuantity(productDTO.getQuantity());

        // Assuming categoryId and subcategoryId are IDs for Category and SubCategory entities
        Category category = new Category();
        category.setCategoryId(productDTO.getCategoryId());
        product.setCategory(category);

        SubCategory subcategory = new SubCategory();
        subcategory.setSubcategoryId(productDTO.getSubcategoryId());
        product.setSubcategory(subcategory);

        return productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCode(product.getCode());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());

        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getCategoryId());
            productDTO.setCategoryName(product.getCategory().getCategoryName());
        }
        if (product.getSubcategory() != null) {
            productDTO.setSubcategoryId(product.getSubcategory().getSubcategoryId());
            productDTO.setSubcategoryName(product.getSubcategory().getSubcategoryName());
        }

        return productDTO;
    }

    @Override
    public ExcelMetaDataDTO prepareExcelData() {
        final var excelMetadataDTO = new ExcelMetaDataDTO();
        excelMetadataDTO.setTableName("Products");
        excelMetadataDTO.setHeaders(List.of("ID", "Name", "Code", "Quantity", "Price", "Category ID", "Category Name", "Subcategory ID", "Subcategory Name"));
        final var products = getAllProduct();
        List<Map<String, String>> metadata = new ArrayList<>();

        for (ProductDTO product : products) {
            Map<String, String> data = new HashMap<>();
            data.put("ID", product.getId().toString());
            data.put("Name", product.getName());
            data.put("Code", product.getCode());
            data.put("Quantity", product.getQuantity().toString());
            data.put("Price", product.getPrice().toString());
            data.put("Category ID", product.getCategoryId().toString());
            data.put("Category Name", product.getCategoryName());
            data.put("Subcategory ID", product.getSubcategoryId().toString());
            data.put("Subcategory Name", product.getSubcategoryName());
            metadata.add(data);
        }
        excelMetadataDTO.setDatas(metadata);
        return excelMetadataDTO;
    }

    @Override
    public ResourceDTO exportExcel() {
        final var resourceDTO = excelService.exportExcel(prepareExcelData());
        resourceDTO.setFileName("Products");
        return resourceDTO;
    }
}