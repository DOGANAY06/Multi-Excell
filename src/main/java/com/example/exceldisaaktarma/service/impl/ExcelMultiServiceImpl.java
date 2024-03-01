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

@Service // Spring tarafından yönetilen bir servis olduğunu belirtir.
@RequiredArgsConstructor // Lombok kütüphanesi ile otomatik olarak constructor oluşturulmasını sağlar.
public class ExcelMultiServiceImpl implements ExcelMultiService {

    // ProductService, CategoryService ve SubCategoryService'yi enjekte etmek için final değişkenler.
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    // Birden fazla sayfa içeren Excel dosyasını oluşturan metot.
    public Resource exportMultiSheetExcel() {
        Workbook workbook = new XSSFWorkbook(); // Apache POI kütüphanesiyle bir Excel çalışma kitabı oluşturur.

        addProductsSheet(workbook); // Ürünler sayfasını Excel'e ekler.
        addCategoriesSheet(workbook); // Kategoriler sayfasını Excel'e ekler.
        addSubCategoriesSheet(workbook); // Alt kategoriler sayfasını Excel'e ekler.

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream); // Workbook'ü bir byte dizisine yazarak Excel dosyasını oluşturur.
            return new ByteArrayResource(outputStream.toByteArray()); // Oluşturulan Excel dosyasını bir ByteArrayResource olarak döndürür.
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create Excel file"); // Excel dosyası oluşturulamazsa bir hata fırlatır.
        }
    }

    // Ürünler sayfasını Excel'e ekleyen metot.
    private void addProductsSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Products"); // "Products" adında bir Excel sayfası oluşturur.
        List<ProductDTO> products = productService.getAllProduct(); // Tüm ürünleri ProductService'den alır.
        addHeaders(sheet, "ID", "Name", "Code", "Price", "Quantity", "Category", "SubCategory"); // Başlıkları ekler.

        int rowIdx = 1; // Başlıkların altından başlamak için satır indeksi.
        for (ProductDTO product : products) { // Her ürün için döngü.
            Row row = sheet.createRow(rowIdx++); // Satır oluşturur ve satır indeksini artırır.
            // Ürün özelliklerini hücrelere ekler.
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getCode());
            row.createCell(3).setCellValue(product.getPrice().doubleValue());
            row.createCell(4).setCellValue(product.getQuantity());
            row.createCell(5).setCellValue(product.getCategoryName());
            row.createCell(6).setCellValue(product.getSubcategoryName());
        }
    }

    // Kategoriler sayfasını Excel'e ekleyen metot.
    private void addCategoriesSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Categories"); // "Categories" adında bir Excel sayfası oluşturur.
        List<CategoryDTO> categories = categoryService.getAllCategory(); // Tüm kategorileri CategoryService'den alır.
        addHeaders(sheet, "ID", "Name", "Products", "SubCategories"); // Başlıkları ekler.

        int rowIdx = 1; // Başlıkların altından başlamak için satır indeksi.
        for (CategoryDTO category : categories) { // Her kategori için döngü.
            Row row = sheet.createRow(rowIdx++); // Satır oluşturur ve satır indeksini artırır.
            // Kategori özelliklerini hücrelere ekler.
            row.createCell(0).setCellValue(category.getCategoryId());
            row.createCell(1).setCellValue(category.getCategoryName());
            row.createCell(2).setCellValue(String.join(",", category.getProductNames()));
            row.createCell(3).setCellValue(String.join(",", category.getSubCategoryNames()));
        }
    }

    // Alt kategoriler sayfasını Excel'e ekleyen metot.
    private void addSubCategoriesSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("SubCategories"); // "SubCategories" adında bir Excel sayfası oluşturur.
        List<SubCategoryDTO> subCategories = subCategoryService.getAllSubCategory(); // Tüm alt kategorileri SubCategoryService'den alır.
        addHeaders(sheet, "ID", "Name", "Products", "Category"); // Başlıkları ekler.

        int rowIdx = 1; // Başlıkların altından başlamak için satır indeksi.
        for (SubCategoryDTO subCategory : subCategories) { // Her alt kategori için döngü.
            Row row = sheet.createRow(rowIdx++); // Satır oluşturur ve satır indeksini artırır.
            // Alt kategori özelliklerini hücrelere ekler.
            if (subCategory.getSubcategoryId() != null) {
                row.createCell(0).setCellValue(subCategory.getSubcategoryId());
            } else {
                row.createCell(0).setCellValue(""); // SubcategoryId null ise boş bir değer atar.
            }
            row.createCell(1).setCellValue(subCategory.getSubcategoryName());
            row.createCell(2).setCellValue(String.join(",", subCategory.getCategoryNames()));
            row.createCell(3).setCellValue(String.join(",", subCategory.getProductNames()));
        }
    }

    // Başlık satırını ekleyen metot.
    private void addHeaders(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(0); // Başlık satırını oluşturur.
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle(); // Başlık hücrelerinin stili.
        Font headerFont = sheet.getWorkbook().createFont(); // Başlık fontu.
        headerFont.setBold(true); // Kalın font kullanılır.
        headerCellStyle.setFont(headerFont); // Başlık hücrelerine fontu uygular.

        for (int i = 0; i < headers.length; i++) { // Her başlık için döngü.
            Cell cell = headerRow.createCell(i); // Hücre oluşturur.
            cell.setCellValue(headers[i]); // Başlık değerini ekler.
            cell.setCellStyle(headerCellStyle); // Hücreye stili uygular.
        }
    }
}

