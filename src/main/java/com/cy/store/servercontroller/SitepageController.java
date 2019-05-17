package com.cy.store.servercontroller;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.common.CommonOperation;
import com.cy.store.exception.JsonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sitepage")
public class SitepageController {

    @Value("${file.sitepage-image-path}")
    private String imageSavePath;

    @RequestMapping(value = {"", "/index", "/list"})
    public String list(){
        return "/admin/site_list";
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
}
