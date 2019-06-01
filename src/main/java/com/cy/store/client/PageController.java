package com.cy.store.client;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.ClientConfig;
import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController extends ClientConfig {

    //自定义页面

    @Autowired
    private SitepageService sitepageService;

    @RequestMapping(value = {"/", "", "/index", "/index/"})
    public String index(ModelMap modelMap){
        JSONObject page = new JSONObject();
        page.put("title", "首页-"+systemTitle);
        page.put("flag", "index");
        modelMap.addAttribute("page", page);
        return webHtml+"index";
    }

    @RequestMapping(value = "/page/{param}")
    public String page(@PathVariable("param") String param, ModelMap modelMap){
        Sitepage sitepage = sitepageService.get(param);
        JSONObject page = new JSONObject();
        page.put("title", "故事-"+systemTitle);
        page.put("flag", "index");
        page.putAll(CommonOperation.obj2Json(sitepage));
        modelMap.addAttribute("page", page);
        return webHtml+"page";
    }

    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public String story(@RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                        ModelMap modelMap){
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

        List<Sitepage> list = sitepageService.getList(filter);

        JSONObject pageData = new JSONObject();
        pageData.put("title", "故事-"+systemTitle);
        pageData.put("flag", "index");
        pageData.put("list", list);
        modelMap.addAttribute("page", page);

        return webHtml+"story";
    }
}
