package com.cy.store.config;

import org.springframework.ui.ModelMap;

import java.util.Map;

//后台配置
public class AdminConfig {


    protected static String adminAccount = "ADMIN_ACCOUNT";

    protected static String adminGroup = "ADMIN_GROUP";

    protected static String adminId = "ADMIN_ID";

    protected static String adminAuth = "ADMIN_AUTH";

    protected static String adminSession = "ADMIN_SESSION";

    protected static String verCode = "ADMIN_VERCODE";

    protected static Integer pageSize = 15;

    //title
    protected static String systemTitle = "后台管理系统";

    protected static String systemMenuTitle = "系统配置-";

    protected static String adminModuleTitle = "管理员-";

    protected static String admingroupModuleTitle = "管理员组-";

    protected static String adminlogModuleTitle = "管理员日志-";

    protected static String siteMenuTitle = "网页配置-";

    protected static String sitepageModuleTitle = "页面管理-";

    protected static String pagetplModuleTitle = "模板管理-";

    protected static String resourceMenuTitle = "资料管理-";

    protected static String articleModuleTitle = "文章-";

    protected static String pictureModuleTitle = "图片-";

    protected static String productMenuTitle = "产品管理-";

    protected static String productModuleTitle = "产品-";

    protected static String categoryModuleTitle = "分类-";

    protected static String membershipMenuTitle = "会员管理-";

    protected static String indexModuleTitle = "首页-";

    protected static String listPageTitle  = "列表-";

    protected static String addPageTitle = "新增-";

    protected static String editPageTitle = "编辑-";

    protected static String authPageTitle = "权限配置-";


    protected static String fileType = "picture|article|product|category";

    protected static String baseSavePath = "C:/www/upload/";

    protected static String articleSavePath = "C:/www/upload/article/";

    protected static String pictureSavePath = "C:/www/upload/picture/";

    protected static String webHtml = "/web/";

    protected static String sysAccount = "System";

    protected static String sysPassword = "123456";


    protected Map<String, Object> setPagenation(Map<String, Object> params){
        Map<String, Object> param = params;
        if(param.get("page") == null || param.get("page").equals("0")) param.put("page", 1);
        param.put("currentPage", param.get("page"));
        param.put("pagesize", pageSize);
        Integer page = Integer.parseInt(param.get("page").toString());
        page = (page-1)*pageSize;
        param.put("page", page);

        if(param.get("totalCount") == null ) param.put("totalCount", 0);
        int totalCount = Integer.parseInt(param.get("totalCount").toString());
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }
        param.put("pageCount", pageCount);

        return param;
    }

}
