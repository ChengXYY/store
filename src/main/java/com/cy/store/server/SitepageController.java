package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import com.cy.store.utils.CommonOperation;
import com.cy.store.exception.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sitepage")
public class SitepageController {

    @Value("${file.sitepage-image-path}")
    private String imageSavePath;

    @Value("${list.pagesize}")
    private Integer pageSize;

    @Autowired
    private SitepageService sitepageService;

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

        model.addAttribute("pageTitle","管理员日志 - 系统设置 - 后台管理系统");
        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "adminlog");
        return "/admin/site_list";
    }

    @RequestMapping("/add")
    public String add(){
        return "/admin/site_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(String code, String title, String content, HttpSession session){
        try {
            return null;

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id", required = true)String id){
        if(id.isEmpty()){
            return "/error/404";
        }
        try {
            return "/admin/site_edit";
        }catch (JsonException e){
            e.toJson();
            return "/error/404";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(String id, String code, String title, String content, HttpSession session){
        try {
            return null;

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){
        JSONObject result = new JSONObject();
        try {
            result = CommonOperation.uploadFile(file, imageSavePath);
            result.put("path", "/sitepage/getimg?filename="+result.get("realname"));
        }catch (JsonException e){
            result = e.toJson();
        }
        return  result;
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
            return "/web/site_preview";

        }catch (JsonException e){
            return "/error/404";
        }
    }
}
