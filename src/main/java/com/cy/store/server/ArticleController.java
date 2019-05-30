package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Article;
import com.cy.store.service.ArticleService;
import com.cy.store.utils.CommonOperation;
import com.cy.store.config.AdminConfig;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController extends AdminConfig {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "code", required = false)String code,
                       @RequestParam(value = "title", required = false)String title,
                       @RequestParam(value = "article", defaultValue = "1", required = false)Integer page,
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
        int totalCount = articleService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("article", (page-1)*pageSize);
        filter.put("pagesize", pageSize);

        List<Article> list = articleService.getList(filter);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);

        model.addAttribute("list", list);
        model.addAttribute("code", code);
        model.addAttribute("title", title);

        model.addAttribute("pageTitle",listPageTitle+articleModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        model.addAttribute("LeftMenuFlag", "article");
        return "/admin/article_list";
    }

    @RequestMapping("/add")
    public String add(ModelMap model){
        //获取模板列表
        model.addAttribute("pageTitle",addPageTitle+articleModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        return "/admin/article_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Article article, HttpSession session){
        article.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return articleService.add(article);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id", required = true)Integer id, ModelMap model){

        try {

            Article article = articleService.get(id);
            model.addAttribute("article", article);
            model.addAttribute("pageTitle",editPageTitle+articleModuleTitle+systemTitle);
            model.addAttribute("TopMenuFlag", "resource");

            return "/admin/article_edit";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Article article){
        try {
            return articleService.edit(article);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject remove(@RequestParam(value = "id", required = true)Integer id){
        try {
            return articleService.remove(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){

        JSONObject result = new JSONObject();
        try {
            result = CommonOperation.uploadFile(file, articleSavePath);
            result.put("path", "/getimg?filename="+result.get("realname"));
        }catch (JsonException e){
            result = e.toJson();
        }
        return  result;
    }
    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public JSONObject get(@RequestParam(value = "code")String code){
        try {
            Article article = articleService.get(code);
            return CommonOperation.success(article);
        }catch (JsonException e){
            return e.toJson();
        }
    }
}
