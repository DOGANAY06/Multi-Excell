package com.example.exceldisaaktarma.service;

import com.example.exceldisaaktarma.dto.ExcelMetaDataDTO;
import com.example.exceldisaaktarma.dto.ProductDTO;
import com.example.exceldisaaktarma.dto.ResourceDTO;
import com.example.exceldisaaktarma.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(ProductDTO productDTO);

    List<ProductDTO> getAllProduct();

    ExcelMetaDataDTO prepareExcelData();

    public ResourceDTO exportExcel();
}
