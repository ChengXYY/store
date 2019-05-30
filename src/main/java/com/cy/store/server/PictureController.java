package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Picture;
import com.cy.store.service.PictureService;
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
@RequestMapping("/picture")
public class PictureController extends AdminConfig {

    @Autowired
    private PictureService pictureService;

    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "code", required = false)String code,
                       @RequestParam(value = "title", required = false)String title,
                       @RequestParam(value = "picture", defaultValue = "1", required = false)Integer page,
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
        int totalCount = pictureService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("picture", (page-1)*pageSize);
        filter.put("pagesize", pageSize);

        List<Picture> list = pictureService.getList(filter);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);

        model.addAttribute("list", list);
        model.addAttribute("code", code);
        model.addAttribute("title", title);

        model.addAttribute("pageTitle",listPageTitle+pictureModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        model.addAttribute("LeftMenuFlag", "picture");
        return "/admin/picture_list";
    }

    @RequestMapping("/add")
    public String add(ModelMap model){
        //获取模板列表       
        model.addAttribute("pageTitle",addPageTitle+pictureModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        return "/admin/picture_add";
    }

    @ResponseBody
    @RequestMapping(value = "/add/submit", method = RequestMethod.POST)
    public JSONObject add(Picture picture, HttpSession session){
        picture.setCreateby(session.getAttribute(adminAccount).toString());
        try {
            return pictureService.add(picture);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "id")Integer id, ModelMap model){

        try {

            Picture picture = pictureService.get(id);
            model.addAttribute("picture", picture);
            model.addAttribute("pageTitle",editPageTitle+pictureModuleTitle+systemTitle);
            model.addAttribute("TopMenuFlag", "resource");

            return "/admin/picture_edit";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Picture picture){
        try {
            return pictureService.edit(picture);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject remove(@RequestParam(value = "id")Integer id){
        try {
            return pictureService.remove(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){

        try {
            JSONObject result = CommonOperation.uploadFile(file, pictureSavePath);
            result.put("url", "/getimg?filename="+result.get("realname"));
            result.put("name", result.get("filename"));
            result.remove("realname");
            result.remove("filename");
            return result;
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public JSONObject get(@RequestParam(value = "code")String code){
        try {
            Picture pic = pictureService.get(code);
            return CommonOperation.success(pic);
        }catch (JsonException e){
            return e.toJson();
        }
    }
}

