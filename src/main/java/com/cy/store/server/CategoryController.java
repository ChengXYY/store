package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.AdminConfig;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Category;
import com.cy.store.service.CategoryService;
import com.cy.store.utils.CommonOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/category")
public class CategoryController extends AdminConfig {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = {"", "/", "/index", "/list"}, method = RequestMethod.GET)
    public String list(@RequestParam Map<String, Object> param,
                       HttpServletRequest request,
                       ModelMap model){
        String currentUrl = request.getRequestURI();
        if(param.get("code")!=null && StringUtils.isNotBlank(param.get("code").toString())){
            currentUrl = CommonOperation.setUrlParam(currentUrl, "code", param.get("code").toString());
        }
        if(param.get("title")!=null && StringUtils.isNotBlank(param.get("title").toString())){
            currentUrl = CommonOperation.setUrlParam(currentUrl, "title", param.get("title").toString());
        }
        param.put("currentUrl", currentUrl);

        int totalCount = categoryService.getCount(param);
        param.put("totalCount", totalCount);
        param = setPagenation(param);

        List<Category> list = categoryService.getList(param);

        model.addAllAttributes(param);
        model.addAttribute("list", list);

        model.addAttribute("pageTitle",listPageTitle+categoryModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "product");
        model.addAttribute("LeftMenuFlag", "category");
        return "/admin/category_list";
    }

    @RequestMapping(value = "/add")
    public String add(ModelMap modelMap){
        modelMap.addAttribute("pageTitle",addPageTitle+categoryModuleTitle+systemTitle);
        modelMap.addAttribute("TopMenuFlag", "product");
        return "/admin/category_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Category category, HttpSession session){
        category.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return categoryService.add(category);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id")Integer id, ModelMap model){

        try {

            Category category = categoryService.get(id);
            model.addAttribute("category", category);
            model.addAttribute("pageTitle",editPageTitle+categoryModuleTitle+systemTitle);
            model.addAttribute("TopMenuFlag", "resource");

            return "/admin/category_edit";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Category category){
        try {
            return categoryService.edit(category);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject remove(@RequestParam(value = "id")Integer id){
        try {
            return categoryService.remove(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject upload(@RequestParam(value = "fileupload")MultipartFile file){

        try {
            JSONObject result = CommonOperation.uploadFile(file, "category");
            result.put("pic", result.get("realname"));
            return result;
        }catch (JsonException e){
            return e.toJson();
        }
    }
}
