package com.example.exceldisaaktarma.service.impl;

import com.example.exceldisaaktarma.dto.CategoryDTO;
import com.example.exceldisaaktarma.dto.ProductDTO;
import com.example.exceldisaaktarma.dto.SubCategoryDTO;
import com.example.exceldisaaktarma.service.CategoryService;
import com.example.exceldisaaktarma.service.ExcelMultiService;
import com.example.exceldisaaktarma.service.ProductService;
import com.example.exceldisaaktarma.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelMultiServiceImpl implements ExcelMultiService {


    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    public Resource exportMultiSheetExcel() {
        Workbook workbook = new XSSFWorkbook();

        addProductsSheet(workbook);
        addCategoriesSheet(workbook);
        addSubCategoriesSheet(workbook);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create Excel file");
        }
    }

    private void addProductsSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Products");
        List<ProductDTO> products = productService.getAllProduct();
        addHeaders(sheet, "ID", "Name", "Code", "Price", "Quantity", "Category", "SubCategory");

        int rowIdx = 1;
        for (ProductDTO product : products) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getCode());
            row.createCell(3).setCellValue(product.getPrice().doubleValue());
            row.createCell(4).setCellValue(product.getQuantity());
            row.createCell(5).setCellValue(product.getCategoryName());
            row.createCell(6).setCellValue(product.getSubcategoryName());
        }
    }

    private void addCategoriesSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Categories");
        List<CategoryDTO> categories = categoryService.getAllCategory();
        addHeaders(sheet, "ID", "Name", "Products", "SubCategories");

        int rowIdx = 1;
        for (CategoryDTO category : categories) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(category.getCategoryId());
            row.createCell(1).setCellValue(category.getCategoryName());
            row.createCell(2).setCellValue(String.join(",", category.getProductNames()));
            row.createCell(3).setCellValue(String.join(",", category.getSubCategoryNames()));
        }
    }

    private void addSubCategoriesSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("SubCategories");
        List<SubCategoryDTO> subCategories = subCategoryService.getAllSubCategory();
        addHeaders(sheet, "ID", "Name", "Products","Category");

        int rowIdx = 1;
        for (SubCategoryDTO subCategory : subCategories) {
            Row row = sheet.createRow(rowIdx++);
            if (subCategory.getSubcategoryId() != null) {
                row.createCell(0).setCellValue(subCategory.getSubcategoryId());
            } else {
                row.createCell(0).setCellValue(""); // veya başka bir değer atayabilirsiniz
            }
            row.createCell(1).setCellValue(subCategory.getSubcategoryName());
            row.createCell(2).setCellValue(String.join(",", subCategory.getCategoryNames()));
            row.createCell(3).setCellValue(String.join(",", subCategory.getProductNames()));
        }
    }

    private void addHeaders(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }
}
