package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.aop.Permission;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Picture;
import com.cy.store.service.PictureService;
import com.cy.store.utils.CommonOperation;
import com.cy.store.config.AdminConfig;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/picture")
@Permission("1002")
public class PictureController extends AdminConfig {

    @Autowired
    private PictureService pictureService;

    @Permission("2122")
    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
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

        int totalCount = pictureService.getCount(param);
        param.put("totalCount", totalCount);
        param = setPagenation(param);

        List<Picture> list = pictureService.getList(param);

        model.addAllAttributes(param);
        model.addAttribute("list", list);

        model.addAttribute("pageTitle",listPageTitle+pictureModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        model.addAttribute("LeftMenuFlag", "picture");
        return "/admin/picture_list";
    }

    @Permission("2122")
    @RequestMapping("/add")
    public String add(ModelMap model){
        //获取模板列表       
        model.addAttribute("pageTitle",addPageTitle+pictureModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "resource");
        return "/admin/picture_add";
    }

    @Permission("2122")
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

    @Permission("2122")
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

    @Permission("2122")
    @ResponseBody
    @RequestMapping(value = "/edit/submit", method = RequestMethod.POST)
    public JSONObject edit(Picture picture){
        try {
            return pictureService.edit(picture);

        }catch (JsonException e){
            return e.toJson();
        }
    }

    @Permission("2122")
    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JSONObject remove(@RequestParam(value = "id")Integer id){
        try {
            return pictureService.remove(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @Permission("2122")
    @ResponseBody
    @RequestMapping("/upload")
    public JSONObject uploadIamge(@RequestParam(value = "fileupload")MultipartFile file){

        try {
            JSONObject result = CommonOperation.uploadFile(file, "picture");
            result.put("url", result.get("realname"));
            result.put("name", result.get("filename"));
            result.remove("realname");
            result.remove("filename");
            return result;
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @Permission("2122")
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

    @Permission("2122")
    @ResponseBody
    @RequestMapping("/batchremove")
    public JSONObject batchRemove(@RequestParam(value = "ids")String ids){
        try {
            return pictureService.remove(ids);
        }catch (JsonException e){
            return e.toJson();
        }
    }
}

