package com.example.exceldisaaktarma.controller;


import com.example.exceldisaaktarma.service.ExcelMultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelMultiController {

    private final ExcelMultiService excelMultiService;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportExcel() {
        Resource resource = excelMultiService.exportMultiSheetExcel();
        // Excel dosyasını indirme isteği olarak döndürün
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=multi_sheet_excel.xlsx")
                .body(resource);
    }
}
