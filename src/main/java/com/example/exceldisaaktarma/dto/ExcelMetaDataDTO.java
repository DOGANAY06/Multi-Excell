package com.example.exceldisaaktarma.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExcelMetaDataDTO {
    private String tableName;
    private List<String> headers;
    private List<Map<String, String>> datas;
}
