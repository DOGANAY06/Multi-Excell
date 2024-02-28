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

    @Override
    public ResourceDTO exportExcel(ExcelMetaDataDTO excelMetadataDTO) {
        Resource resource = prepareExcel(excelMetadataDTO);
        final var resourceDTO = new ResourceDTO();
        resourceDTO.setResource(resource);
        resourceDTO.setMediaType(MediaType.parseMediaType
                ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        return resourceDTO;
    }

    private Resource prepareExcel(ExcelMetaDataDTO excelMetadataDTO) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(excelMetadataDTO.getTableName());

        prepareHeaders(workbook, sheet, excelMetadataDTO.getHeaders());
        fillTable(workbook, sheet, excelMetadataDTO.getDatas(), excelMetadataDTO.getHeaders());

        try (ByteArrayOutputStream byteArrayOutputStream
                     = new ByteArrayOutputStream()) {

            workbook.write(byteArrayOutputStream);
            return new
                    ByteArrayResource
                    (byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException
                    ("ERROR");
        }
    }

    private void fillTable(Workbook workbook, Sheet sheet,
                           List<Map<String, String>> datas, List<String> headers) {

        int rowNo = 1;
        Font font = workbook.createFont();
        font.setFontName("Arial");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        for (Map<String, String> data : datas) {
            Row row = sheet.createRow(rowNo);
            for (int columnNo = 0; columnNo < headers.size(); columnNo++) {
                fillCell(sheet, row, columnNo,
                        String.valueOf(data.get(headers.get(columnNo))), cellStyle);
            }
            rowNo++;
        }
    }

    private void fillCell(Sheet sheet, Row row, int columnNo,
                          String value, CellStyle cellStyle) {

        Cell cell = row.createCell(columnNo);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
        sheet.autoSizeColumn(columnNo);
    }

    private void prepareHeaders(Workbook workbook,
                                Sheet sheet, List<String> headers) {

        Row headerRow = sheet.createRow(0);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Arial");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);

        int columnNo = 0;
        for (String header : headers) {
            Cell headerCell = headerRow.createCell(columnNo++);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(cellStyle);
        }
    }
}