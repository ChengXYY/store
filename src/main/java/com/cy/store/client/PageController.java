package com.cy.store.client;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.ClientConfig;
import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        pageData.put("title", "首页-"+systemTitle);
        pageData.put("topflag", "index");
        pageData.put("intro", "翡翠");
        pageData.put("subintro", "翡翠（jadeite）， 也称翡翠玉、翠玉、缅甸玉，是玉的一种。翡翠的正确定义是以硬玉矿物为主的辉石类矿物组成的纤维状集合体 ，但是翡翠并不等于硬玉");
        pageData.put("toplink", "");
        modelMap.addAttribute("page", pageData);
        return webHtml+"index";
    }

    @RequestMapping(value = "/page/{param}")
    public String page(@PathVariable("param") String param, ModelMap modelMap){
        Sitepage sitepage = sitepageService.get(param);
        pageData.put("title", "故事-"+systemTitle);
        pageData.put("topflag", "index");
        pageData.putAll(CommonOperation.obj2Json(sitepage));
        modelMap.addAttribute("page", pageData);
        return webHtml+"page";
    }

    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public String story(@RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                        HttpServletRequest req,
                        ModelMap modelMap){
        /*
        Map<String, Object> filter = new HashMap<>();
        if(page == null || page<1){
            page = 1;
        }
        int totalCount = sitepageService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);

        List<Sitepage> list = sitepageService.getList(filter); */


        String requestUrl =  req.getRequestURI();

        pageData.put("title", "故事-"+systemTitle);
        pageData.put("topflag", "story");
     //   pageData.put("list", list);
        pageData.put("currentPage", page);
        pageData.put("pageCount", 15);
        pageData.put("totalCount", 143);
        pageData.put("currentUrl", requestUrl);

        pageData.put("intro", "翡翠");
        pageData.put("subintro", "翡翠（jadeite）， 也称翡翠玉、翠玉、缅甸玉，是玉的一种。翡翠的正确定义是以硬玉矿物为主的辉石类矿物组成的纤维状集合体 ，但是翡翠并不等于硬玉");

        modelMap.addAttribute("page", pageData);

        return webHtml+"story";
    }

    @RequestMapping("/product")
    public String product(@RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                          HttpServletRequest req,
                          ModelMap modelMap){
        String requestUrl =  req.getRequestURI();

        pageData.put("title", "产品-"+systemTitle);
        pageData.put("topflag", "product");
        //   pageData.put("list", list);
        pageData.put("currentPage", page);
        pageData.put("pageCount", 15);
        pageData.put("totalCount", 143);
        pageData.put("currentUrl", requestUrl);

        pageData.put("intro", "翡翠");
        pageData.put("subintro", "翡翠（jadeite）， 也称翡翠玉、翠玉、缅甸玉，是玉的一种。翡翠的正确定义是以硬玉矿物为主的辉石类矿物组成的纤维状集合体 ，但是翡翠并不等于硬玉");

        modelMap.addAttribute("page", pageData);

        return webHtml+"product";
    }

    @RequestMapping("/blog")
    public String blog(ModelMap modelMap){
        pageData.put("title", "新闻-"+systemTitle);
        pageData.put("topflag", "blog");
        pageData.put("intro", "翡翠");
        pageData.put("toplink", "");
        pageData.put("subintro", "翡翠（jadeite）， 也称翡翠玉、翠玉、缅甸玉，是玉的一种。翡翠的正确定义是以硬玉矿物为主的辉石类矿物组成的纤维状集合体 ，但是翡翠并不等于硬玉");
        modelMap.addAttribute("page", pageData);
        return webHtml+"blog";
    }
}
