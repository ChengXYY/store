package com.cy.store.server;

import com.cy.store.aop.Permission;
import com.cy.store.config.AdminConfig;
import com.cy.store.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController extends AdminConfig {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdmingroupService admingroupService;

    @Autowired
    private AdminlogService adminlogService;

    @Autowired
    private SitepageService sitepageService;

    @Autowired
    private PagetplService pagetplService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private CategoryService categoryService;



    @RequestMapping(value = {"/admin/index", "/admin/", "/admin"})
    public String index(ModelMap model){
        model.addAttribute("TopMenuFlag", "index");
        model.addAttribute("pageTitle",indexModuleTitle+systemTitle);
        return "/admin/index";
    }

    @Permission("9001")
    @RequestMapping(value = {"/system", "/system/index", "/system/"})
    public String system(ModelMap modelMap){
        Map<String, Object> filter = new HashMap<>();
        int adminCount = adminService.getCount(filter);
        int groupCount = admingroupService.countAll(0);

        filter.put("content", "管理员登录");
        int loginCount = adminlogService.getCount(filter);
        modelMap.addAttribute("adminCount", adminCount);
        modelMap.addAttribute("groupCount", groupCount);
        modelMap.addAttribute("loginCount", loginCount);
        modelMap.addAttribute("TopMenuFlag", "system");
        modelMap.addAttribute("pageTitle",systemMenuTitle+systemTitle);
        return "/admin/system_index";
    }

    @Permission("1001")
    @RequestMapping(value = {"/site", "/site/index", "/site/"})
    public String sitepage(ModelMap modelMap){
        Map<String, Object> filter = new HashMap<>();
        int tplCount = pagetplService.getCount(filter);
        int siteCount = sitepageService.getCount(filter);
        modelMap.addAttribute("tplCount", tplCount);
        modelMap.addAttribute("siteCount", siteCount);
        modelMap.addAttribute("TopMenuFlag", "sitepage");
        modelMap.addAttribute("pageTitle",siteMenuTitle+systemTitle);
        return "/admin/sitepage_index";
    }

    @Permission("1002")
    @RequestMapping(value = {"/resource", "/resource/index", "/resource/"})
    public String resource(ModelMap modelMap){
        Map<String, Object> filter = new HashMap<>();
        int pictureCount = pictureService.getCount(filter);
        int articleCount = articleService.getCount(filter);
        modelMap.addAttribute("pictureCount", pictureCount);
        modelMap.addAttribute("articleCount", articleCount);
        modelMap.addAttribute("TopMenuFlag", "resource");
        modelMap.addAttribute("pageTitle",resourceMenuTitle+systemTitle);
        return "/admin/resource_index";
    }

    @Permission("1003")
    @RequestMapping(value = {"/products", "/products/index", "/products/"})
    public String product(ModelMap modelMap){
        Map<String, Object> filter = new HashMap<>();
        int categoryCount = categoryService.getCount(filter);
        modelMap.addAttribute("categoryCount", categoryCount);
        modelMap.addAttribute("TopMenuFlag", "product");
        modelMap.addAttribute("pageTitle",productMenuTitle+systemTitle);
        return "/admin/product_index";
    }

    @Permission("1004")
    @RequestMapping(value = {"/membership", "/membership/index", "/membership/"})
    public String membership(ModelMap modelMap){
        modelMap.addAttribute("TopMenuFlag", "membership");
        modelMap.addAttribute("pageTitle",membershipMenuTitle+systemTitle);
        return "/admin/membership_index";
    }
}
