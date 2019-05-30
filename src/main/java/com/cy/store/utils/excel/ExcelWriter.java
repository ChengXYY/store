package com.cy.store.utils.excel;

import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelWriter {
    public ExcelWriter() {
    }

    public static int writeFile(String filename, Map<String, Object> header, List<Map<String, Object>> rows) {
        FileOutputStream out = null;

        short var5;
        try {
            out = new FileOutputStream(filename);
            ExcelTypeEnum type = filename.toLowerCase().endsWith(".xls") ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
            com.alibaba.excel.ExcelWriter writer = new com.alibaba.excel.ExcelWriter(out, type);
            Sheet sheet1 = new Sheet(1);
            sheet1.setSheetName("sheet1");
            List<String> keys = new ArrayList();
            List<List<String>> head = new ArrayList();
            Iterator var9 = header.values().iterator();

            while(var9.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var9.next();
                keys.add(entry.getKey());
                head.add(Arrays.asList(entry.getValue().toString()));
            }

            sheet1.setHead(head);
            List<List<String>> data = new ArrayList();
            Iterator var28 = rows.iterator();

            while(var28.hasNext()) {
                Map<String, Object> m = (Map<String, Object>)var28.next();
                List<String> row = new ArrayList();
                Iterator var13 = keys.iterator();

                while(var13.hasNext()) {
                    String key = (String)var13.next();
                    String v = key==null?"":key;
                    row.add(v);
                }

                data.add(row);
            }

            writer.write0(data, sheet1);
            writer.finish();
            byte var29 = 0;
            return var29;
        } catch (Exception var24) {
            var5 = -905;
        } finally {
            try {
                out.close();
            } catch (IOException var23) {
                ;
            }

        }

        return var5;
    }
}

