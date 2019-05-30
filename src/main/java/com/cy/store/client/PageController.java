package com.cy.store.client;

import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import com.cy.store.config.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends AdminConfig {

    //自定义页面

    @Autowired
    private SitepageService sitepageService;

    @RequestMapping(value = {"/", "", "/index", "/index/"})
    public String index(ModelMap modelMap){
        return "/web/index";
    }

    @RequestMapping(value = "/page/{param}")
    public String page(@PathVariable("param") String param, ModelMap modelMap){
        Sitepage sitepage = sitepageService.get(param);
        modelMap.addAttribute("page", sitepage);
        return webHtml+"page";
    }
}
