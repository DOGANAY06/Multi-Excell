package com.example.exceldisaaktarma.service;

import com.example.exceldisaaktarma.dto.ExcelMetaDataDTO;
import com.example.exceldisaaktarma.dto.ResourceDTO;

public interface ExcelService {

    ResourceDTO exportExcel(ExcelMetaDataDTO excelMetadataDTO);
}