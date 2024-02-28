package com.example.exceldisaaktarma.controller;

import com.example.exceldisaaktarma.dto.ProductDTO;
import com.example.exceldisaaktarma.dto.ResourceDTO;
import com.example.exceldisaaktarma.entity.Product;
import com.example.exceldisaaktarma.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.create(productDTO));
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportProducts() {
        ResourceDTO resourceDTO = productService.exportExcel();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                "attachment; filename=" + resourceDTO.getFileName() + ".xlsx");

        return ResponseEntity.ok().contentType(resourceDTO.getMediaType())
                .headers(httpHeaders).body((Resource) resourceDTO.getResource());
    }
}