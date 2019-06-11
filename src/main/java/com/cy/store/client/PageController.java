package com.cy.store.client;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.ClientConfig;
import com.cy.store.service.SitepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController extends ClientConfig {

    private JSONObject pageData = new JSONObject();

    //自定义页面

    @Autowired
    private SitepageService sitepageService;

    @RequestMapping(value = {"/", "", "/index", "/index/"})
    public String index(ModelMap modelMap){
        pageData.put("title", systemTitle);
        pageData.put("topflag", "index");
        modelMap.addAttribute("page", pageData);
        return webHtml+"index";
    }

    @RequestMapping("/product")
    public String product(@RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                          HttpServletRequest req,
                          ModelMap modelMap){
        String requestUrl =  req.getRequestURI();

        pageData.put("title", "珠宝·翡翠 - "+systemTitle);
        pageData.put("topflag", "product");
        //   pageData.put("list", list);
        pageData.put("currentPage", page);
        pageData.put("pageCount", 15);
        pageData.put("totalCount", 143);
        pageData.put("currentUrl", requestUrl);
  modelMap.addAttribute("page", pageData);

        return webHtml+"product";
    }

    @RequestMapping("/story")
    public String blog(ModelMap modelMap){
        pageData.put("title", "新闻·故事 - "+systemTitle);
        pageData.put("topflag", "blog");
        modelMap.addAttribute("page", pageData);
        return webHtml+"story";
    }
}
