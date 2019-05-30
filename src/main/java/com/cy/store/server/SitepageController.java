package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Sitepage;
import com.cy.store.service.PagetplService;
import com.cy.store.service.SitepageService;
import com.cy.store.exception.JsonException;
import com.cy.store.config.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sitepage")
public class SitepageController extends AdminConfig {

    @Autowired
    private SitepageService sitepageService;

    @Autowired
    private PagetplService pagetplService;

    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "code", required = false)String code,
                       @RequestParam(value = "title", required = false)String title,
                       @RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                       HttpServletRequest request,
                       ModelMap model){
        Map<String, Object> filter = new HashMap<>();
        if(code!=null && !code.isEmpty()){
            filter.put("code", code);
        }
        if(title!=null && !title.isEmpty()){
            filter.put("title", title);
        }
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

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);

        model.addAttribute("list", list);
        model.addAttribute("code", code);
        model.addAttribute("title", title);

        model.addAttribute("pageTitle",listPageTitle+sitepageModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "sitepage");
        model.addAttribute("LeftMenuFlag", "page");
        return "/admin/site_list";
    }

    @RequestMapping("/add")
    public String add(ModelMap model){
        //获取模板列表
        List<Map<String, Object>> list = pagetplService.getSelectList();
        model.addAttribute("list", list);
        model.addAttribute("pageTitle",addPageTitle+sitepageModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "sitepage");
        return "/admin/site_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Sitepage sitepage, HttpSession session){
        sitepage.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return sitepageService.add(sitepage);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id", required = true)Integer id, ModelMap model){

        try {
            List<Map<String, Object>> list = pagetplService.getSelectList();
            model.addAttribute("list", list);

            Sitepage sitepage = sitepageService.get(id);
            model.addAttribute("page", sitepage);
            model.addAttribute("pageTitle",editPageTitle+sitepageModuleTitle+systemTitle);
            model.addAttribute("TopMenuFlag", "sitepage");

            return "/admin/site_edit";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Sitepage sitepage){
        try {
            return sitepageService.edit(sitepage);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public String preview(@RequestParam(value = "id", required = true)String id, ModelMap model){
        if(id==null || id.isEmpty() || id.equals("0")){
            return "/error/404";
        }
        Integer pageId = Integer.parseInt(id);
        try {
            Sitepage page = sitepageService.get(pageId);
            model.addAttribute("page", page);
            return "/admin/site_preview";

        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject remove(@RequestParam(value = "id", required = true)Integer id){

        try {
            return sitepageService.remove(id);

        }catch (JsonException e){
            return e.toJson();
        }
    }
}
