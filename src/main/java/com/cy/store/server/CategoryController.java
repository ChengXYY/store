package com.cy.store.server;

import com.cy.store.config.AdminConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/category")
public class CategoryController extends AdminConfig {

    @RequestMapping(value = {"", "/", "/index", "/list"}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "page")Integer page,
                       HttpServletRequest request,
                       ModelMap model){
        return "/admin/category_list";
    }
}
