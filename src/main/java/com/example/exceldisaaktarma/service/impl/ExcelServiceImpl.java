package com.example.exceldisaaktarma.service.impl;
import com.example.exceldisaaktarma.dto.ExcelMetaDataDTO;
import com.example.exceldisaaktarma.dto.ResourceDTO;
import com.example.exceldisaaktarma.service.ExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    // Excel dosyasını hazırlar ve kaynak DTO'sunu döndürür
    @Override
    public ResourceDTO exportExcel(ExcelMetaDataDTO excelMetadataDTO) {
        // Excel dosyasını hazırla
        Resource resource = prepareExcel(excelMetadataDTO);
        // Hazırlanan Excel dosyasının MIME türünü ayarla
        final var resourceDTO = new ResourceDTO();
        resourceDTO.setResource(resource);
        resourceDTO.setMediaType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        return resourceDTO;
    }

    // Excel dosyasını hazırlar
    private Resource prepareExcel(ExcelMetaDataDTO excelMetadataDTO) {
        // Yeni bir Excel iş kitabı oluştur
        Workbook workbook = new XSSFWorkbook();
        // İlgili tabloya bir çalışma sayfası ekle
        Sheet sheet = workbook.createSheet(excelMetadataDTO.getTableName());
        // Başlıkları hazırla
        prepareHeaders(workbook, sheet, excelMetadataDTO.getHeaders());
        // Tabloyu doldur
        fillTable(workbook, sheet, excelMetadataDTO.getDatas(), excelMetadataDTO.getHeaders());

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // İş kitabını yazılı bir diziye yaz
            workbook.write(byteArrayOutputStream);
            // Yazılı diziyi kaynak olarak kullan
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR");
        }
    }

    // Tabloyu Excel dosyasına doldurur
    private void fillTable(Workbook workbook, Sheet sheet, List<Map<String, String>> datas, List<String> headers) {
        // Satır numarası başlat
        int rowNo = 1;
        // Yazı tipini ve hücre stili oluştur
        Font font = workbook.createFont();
        font.setFontName("Arial");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        // Her veri satırı için işlem yap
        for (Map<String, String> data : datas) {
            // Yeni bir satır oluştur
            Row row = sheet.createRow(rowNo);
            // Her hücreyi doldur
            for (int columnNo = 0; columnNo < headers.size(); columnNo++) {
                // Hücreyi doldur
                fillCell(sheet, row, columnNo, String.valueOf(data.get(headers.get(columnNo))), cellStyle);
            }
            // Bir sonraki satıra geç
            rowNo++;
        }
    }

    // Hücreyi doldurur
    private void fillCell(Sheet sheet, Row row, int columnNo, String value, CellStyle cellStyle) {
        // Hücre oluştur ve değeri ayarla
        Cell cell = row.createCell(columnNo);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
        // Sütun genişliğini otomatik olarak ayarla
        sheet.autoSizeColumn(columnNo);
    }

    // Başlıkları hazırlar
    private void prepareHeaders(Workbook workbook, Sheet sheet, List<String> headers) {
        // Başlık satırını oluştur
        Row headerRow = sheet.createRow(0);
        // Yazı tipini ve hücre stili oluştur
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Arial");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        // Başlık hücrelerini oluştur ve başlıkları ayarla
        int columnNo = 0;
        for (String header : headers) {
            Cell headerCell = headerRow.createCell(columnNo++);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(cellStyle);
        }
    }
}
