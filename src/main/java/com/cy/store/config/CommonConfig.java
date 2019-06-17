package com.cy.store.config;

import java.util.Map;

public class CommonConfig {


    protected static String fileType = "picture|article|product|category";

    protected static String baseSavePath = "C:/www/upload/";

    protected static Integer pageSize = 15;


    protected Map<String, Object> setPagenation(Map<String, Object> params){
        Map<String, Object> param = params;
        if(param.get("page") == null || param.get("page").equals("0")) param.put("page", 1);
        Integer page = Integer.parseInt(param.get("page").toString());

        param.put("currentPage", page);
        param.put("pagesize", pageSize);

        page = (page-1)*pageSize;
        param.put("page", page);

        if(param.get("totalCount") == null ) param.put("totalCount", 0);
        int totalCount = Integer.parseInt(param.get("totalCount").toString());
        int pageCount = (int)Math.ceil((double)totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }
        param.put("pageCount", pageCount);

        return param;
    }

}
