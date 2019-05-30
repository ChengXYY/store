package com.cy.store.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    private static final Logger log = LoggerFactory.getLogger(ExcelReader.class);

    public ExcelReader() {
    }

    public static ExcelReader.ExcelData readFile(String filename) {
        return readFile(filename, (List)null);
    }

    public static ExcelReader.ExcelData readFile(String filename, final List<String> specifiedFields) {
        ExcelTypeEnum type = getExcelType(filename);
        if (type == null) {
            return new ExcelReader.ExcelData(-900, "文件类型不正确");
        } else {
            InputStream in = getFileInputStream(filename);
            if (in == null) {
                return new ExcelReader.ExcelData(-901, "文件无法打开");
            } else {
                ExcelReader.ExcelData var6;
                try {
                    String msg;
                    try {
                        final ExcelReader.ExcelData excelData = new ExcelReader.ExcelData();
                        com.alibaba.excel.ExcelReader excelReader = new com.alibaba.excel.ExcelReader(in, (Object)null, new AnalysisEventListener<List<Object>>() {
                            public void invoke(List<Object> columns, AnalysisContext context) {
                                if (context.getCurrentRowNum() == 0) {
                                    ExcelReader.initHeaders(excelData, columns, specifiedFields);
                                } else {
                                    Map<String, Object> flowMap = new HashMap<>();
                                    List<String> realFields = excelData.fields;
                                    int n = realFields.size();
                                    int cn = columns.size();

                                    for(int i = 0; i < n; ++i) {
                                        String fieldName = (String)realFields.get(i);
                                        if (!fieldName.startsWith("ignoredColumn")) {
                                            if (i >= cn) {
                                                flowMap.put(fieldName, "");
                                            } else {
                                                Object v = columns.get(i);
                                                flowMap.put(fieldName, v == null ? "" : v.toString());
                                            }
                                        }
                                    }

                                    excelData.rows.add(flowMap);
                                }
                            }

                            public void doAfterAllAnalysed(AnalysisContext context) {
                            }
                        });
                        excelReader.read();
                        var6 = excelData;
                        return var6;
                    } catch (Exception var18) {
                        msg = var18.getMessage();
                        if (msg.equals("title_not_valid")) {
                            var6 = new ExcelReader.ExcelData(-902, "EXCEL标题行格式不正确");
                            return var6;
                        }
                    }

                    if (!msg.equals("field_name_duplicated")) {
                        log.error("read excel exception");
                        var6 = new ExcelReader.ExcelData(-904, "EXCEL读取时出现异常");
                        return var6;
                    }

                    var6 = new ExcelReader.ExcelData(-903, "EXCEL标题行字段名重复");
                } finally {
                    try {
                        in.close();
                    } catch (IOException var17) {
                        ;
                    }

                }

                return var6;
            }
        }
    }

    static void initHeaders(ExcelReader.ExcelData excelData, List<Object> columns, List<String> specifiedFields) {
        int i;
        if (specifiedFields != null) {
            List<String> headers = new ArrayList();

            for(i = 0; i < specifiedFields.size(); ++i) {
                if (i >= columns.size()) {
                    headers.add(specifiedFields.get(i));
                } else {
                    Object v = columns.get(i);
                    headers.add(v == null ? (String)specifiedFields.get(i) : v.toString());
                }
            }

            excelData.headers = headers;
            excelData.fields = specifiedFields;
        } else {
            int n = columns.size();

            for(i = 0; i < n; ++i) {
                String header = columns.get(i).toString();
                String fieldName = parseHeaderFieldName(header);
                if (fieldName.isEmpty()) {
                    fieldName = "ignoredColumn" + i;
                }

                if (excelData.fields.contains(fieldName)) {
                    throw new RuntimeException("field_name_duplicated");
                }

                excelData.headers.add(header);
                excelData.fields.add(fieldName);
            }
        }

    }

    static ExcelTypeEnum getExcelType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(ExcelTypeEnum.XLSX.getValue())) {
            return ExcelTypeEnum.XLSX;
        } else {
            return fileName.endsWith(ExcelTypeEnum.XLS.getValue()) ? ExcelTypeEnum.XLS : null;
        }
    }

    static InputStream getFileInputStream(String fileName) {
        try {
            File importFile = new File(fileName);
            return new FileInputStream(importFile);
        } catch (FileNotFoundException var2) {
            log.error("file not found, filename=" + fileName);
            return null;
        }
    }

    static String parseHeaderFieldName(String content) {
        if (!content.endsWith(")")) {
            return "";
        } else if (content.indexOf("(") > 0 && content.indexOf(")") > 0) {
            int s = content.indexOf("(");
            int e = content.indexOf(")");
            return s >= e ? "" : content.substring(s + 1, e).trim();
        } else {
            return "";
        }
    }

    public static class ExcelData {
        public int retCode;
        public String retMsg;
        public List<String> headers;
        public List<String> fields;
        public List<Map<String, Object>> rows;

        public ExcelData() {
            this.headers = new ArrayList();
            this.fields = new ArrayList();
            this.rows = new ArrayList();
        }

        public ExcelData(int retCode, String retMsg) {
            this.retCode = retCode;
            this.retMsg = retMsg;
        }
    }
}

