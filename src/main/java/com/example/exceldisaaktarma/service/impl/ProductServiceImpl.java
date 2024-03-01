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

@Service // Bu sınıfın bir Spring Service bileşeni olduğunu belirtir.
@RequiredArgsConstructor // Lombok tarafından sağlanan bir annotation; final alanlar için otomatik olarak constructor oluşturur.
public class ProductServiceImpl implements ProductService {

    private final ExcelService excelService; // ExcelService türünde bir nesne enjekte edilir.
    private final ProductRepository productRepository; // ProductRepository türünde bir nesne enjekte edilir.

    @Override
    public Product create(ProductDTO productDTO) { // ProductService arabiriminden gelen create metodunu uygular.
        final var product = new Product(); // Yeni bir Product nesnesi oluşturulur.
        product.setCode(productDTO.getCode()); // Ürün kodu atanır.
        product.setPrice(productDTO.getPrice()); // Ürün fiyatı atanır.
        product.setName(productDTO.getName()); // Ürün adı atanır.
        product.setQuantity(productDTO.getQuantity()); // Ürün miktarı atanır.

        // Ürünün ait olduğu kategori ve alt kategori ID'leri varsayılan olarak atanır.
        Category category = new Category();
        category.setCategoryId(productDTO.getCategoryId());
        product.setCategory(category);

        SubCategory subcategory = new SubCategory();
        subcategory.setSubcategoryId(productDTO.getSubcategoryId());
        product.setSubcategory(subcategory);

        return productRepository.save(product); // Ürün veritabanına kaydedilir ve kaydedilen ürün döndürülür.
    }

    @Override
    public List<ProductDTO> getAllProduct() { // ProductService arabiriminden gelen getAllProduct metodunu uygular.
        List<Product> products = productRepository.findAll(); // Tüm ürünler veritabanından alınır.
        return products.stream().map(this::convertToDTO).collect(Collectors.toList()); // Ürünler DTO'ya dönüştürülür ve liste olarak döndürülür.
    }

    private ProductDTO convertToDTO(Product product) { // Product nesnesini ProductDTO'ya dönüştüren yardımcı metot.
        ProductDTO productDTO = new ProductDTO(); // Yeni bir ProductDTO nesnesi oluşturulur.
        productDTO.setId(product.getId()); // ID atanır.
        productDTO.setCode(product.getCode()); // Ürün kodu atanır.
        productDTO.setName(product.getName()); // Ürün adı atanır.
        productDTO.setPrice(product.getPrice()); // Ürün fiyatı atanır.
        productDTO.setQuantity(product.getQuantity()); // Ürün miktarı atanır.

        // Ürünün ait olduğu kategori ve alt kategori bilgileri atanır (eğer mevcutsa).
        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getCategoryId());
            productDTO.setCategoryName(product.getCategory().getCategoryName());
        }
        if (product.getSubcategory() != null) {
            productDTO.setSubcategoryId(product.getSubcategory().getSubcategoryId());
            productDTO.setSubcategoryName(product.getSubcategory().getSubcategoryName());
        }

        return productDTO; // Dönüştürülen ProductDTO nesnesi döndürülür.
    }

    @Override
    public ExcelMetaDataDTO prepareExcelData() { // ProductService arabiriminden gelen prepareExcelData metodunu uygular.
        final var excelMetadataDTO = new ExcelMetaDataDTO(); // Yeni bir ExcelMetaDataDTO nesnesi oluşturulur.
        excelMetadataDTO.setTableName("Products"); // Excel tablosunun adı atanır.
        excelMetadataDTO.setHeaders(List.of("ID", "Name", "Code", "Quantity", "Price", "Category ID", "Category Name", "Subcategory ID", "Subcategory Name")); // Excel tablosunun başlıkları atanır.
        final var products = getAllProduct(); // Tüm ürünler alınır.
        List<Map<String, String>> metadata = new ArrayList<>(); // Excel veri meta bilgileri için yeni bir liste oluşturulur.

        // Her bir ürün için meta bilgiler oluşturulur ve listeye eklenir.
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
            metadata.add(data); // Oluşturulan veri meta bilgileri listeye eklenir.
        }
        excelMetadataDTO.setDatas(metadata); // ExcelMetaDataDTO nesnesine veri meta bilgileri atanır.
        return excelMetadataDTO; // Oluşturulan ExcelMetaDataDTO nesnesi döndürülür.
    }

    @Override
    public ResourceDTO exportExcel() { // ProductService arabiriminden gelen exportExcel metodunu uygular.
        final var resourceDTO = excelService.exportExcel(prepareExcelData()); // Excel verileri hazırlanır ve dışa aktarılır.
        resourceDTO.setFileName("Products"); // Dosya adı atanır.
        return resourceDTO; // Dışa aktarılan veriler döndürülür.
    }
}
