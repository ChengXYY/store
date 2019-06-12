package com.cy.store.server;

import com.cy.store.config.AdminConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends AdminConfig {

    @RequestMapping(value = {"/admin/index", "/admin/", "/admin"})
    public String index(ModelMap model){
        model.addAttribute("TopMenuFlag", "index");
        model.addAttribute("pageTitle",indexModuleTitle+systemTitle);
        return "/admin/index";
    }

    @RequestMapping(value = {"/system", "/system/index", "/system/"})
    public String system(ModelMap modelMap){
        modelMap.addAttribute("TopMenuFlag", "system");
        modelMap.addAttribute("pageTitle",systemMenuTitle+systemTitle);
        return "/admin/system_index";
    }

    @RequestMapping(value = {"/site", "/site/index", "/site/"})
    public String sitepage(ModelMap modelMap){
        modelMap.addAttribute("TopMenuFlag", "sitepage");
        modelMap.addAttribute("pageTitle",siteMenuTitle+systemTitle);
        return "/admin/sitepage_index";
    }

    @RequestMapping(value = {"/resource", "/resource/index", "/resource/"})
    public String resource(ModelMap modelMap){
        modelMap.addAttribute("TopMenuFlag", "resource");
        modelMap.addAttribute("pageTitle",resourceMenuTitle+systemTitle);
        return "/admin/resource_index";
    }

    @RequestMapping(value = {"/products", "/products/index", "/products/"})
    public String product(ModelMap modelMap){
        modelMap.addAttribute("TopMenuFlag", "product");
        modelMap.addAttribute("pageTitle",productMenuTitle+systemTitle);
        return "/admin/product_index";
    }
}
