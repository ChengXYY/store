package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.aop.Permission;
import com.cy.store.config.AdminConfig;
import com.cy.store.model.Product;
import com.cy.store.utils.CommonOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
@Permission("1003")
public class ProductController extends AdminConfig {


    @Permission("2131")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam Map<String, Object> params,
                       HttpServletRequest request,
                       ModelMap model){

        String currentUrl = request.getRequestURI();

        int totalCount = 0;//sitepageService.getCount(params);
        setPagenation(params);

        List<Product> list = null; //sitepageService.getList(filter);

        model.addAllAttributes(params);
        model.addAttribute("list", list);

        model.addAttribute("pageTitle",listPageTitle+productModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "product");
        model.addAttribute("LeftMenuFlag", "product");
        return "/admin/product_list";
    }

    @Permission("2131")
    @RequestMapping("/add")
    public String add(ModelMap modelMap){
        return "/admin/product_add";
    }

    @Permission("2131")
    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Product product){
        return null;
    }

    @Permission("2131")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id")Integer id,
                       ModelMap modelMap){
        return "/admin/product_edit";
    }



}
