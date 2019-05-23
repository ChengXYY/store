package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Pagetpl;
import com.cy.store.service.PagetplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pagetpl")
public class PagetplController extends BaseController{
    @Autowired
    private PagetplService pagetplService;

    @RequestMapping(value = {"", "/", "index", "list"}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "name", required = false)String name,
                       @RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                       ModelMap model){
        Map<String, Object> filter = new HashMap<>();
        if(name!=null && !name.isEmpty()){
            filter.put("name", name);
        }
        if(page == null || page<1){
            page = 1;
        }
        int totalCount = pagetplService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);

        List<Pagetpl> list = pagetplService.getList(filter);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);

        model.addAttribute("list", list);
        model.addAttribute("name", name);

        model.addAttribute("pageTitle",listPageTitle+pagetplModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "sitepage");
        model.addAttribute("LeftMenuFlag", "tpl");
        return "/admin/tpl_list";
    }

    @RequestMapping("/add")
    public String add(ModelMap model){
        model.addAttribute("pageTitle",addPageTitle+pagetplModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "sitepage");
        return "/admin/tpl_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Pagetpl pagetpl, HttpSession session){
        pagetpl.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return pagetplService.add(pagetpl);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id", required = true)Integer id, ModelMap model){
        try {
            JSONObject tpl = pagetplService.get(id);
            model.addAttribute("tpl", tpl);
            model.addAttribute("pageTitle",addPageTitle+pagetplModuleTitle+systemTitle);
            model.addAttribute("TopMenuFlag", "sitepage");
            return "/admin/tpl_edit";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Pagetpl pagetpl, HttpSession session){
        pagetpl.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return pagetplService.edit(pagetpl);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public JSONObject get(@RequestParam(value = "id", required = true) Integer id){
        try {
            return pagetplService.get(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }
}
