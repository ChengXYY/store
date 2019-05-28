package com.cy.store.common;

import com.cy.store.exception.JsonException;
import com.cy.store.model.Picture;
import com.cy.store.server.BaseController;
import com.cy.store.service.PictureService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ImageController extends BaseController {

    @Autowired
    private PictureService pictureService;

    @RequestMapping(value = "/getimg", method = RequestMethod.GET)
    public void getImage(@RequestParam(value = "filename") String filename,
                         HttpServletRequest request, HttpServletResponse response)throws IOException {
        CommonOperation.getImage(filename, pictureSavePath, request, response);
    }

    @RequestMapping(value = "/getpicture", method = RequestMethod.GET)
    public void getPicture(@RequestParam(value = "code") String code,
                           HttpServletRequest request, HttpServletResponse response)throws IOException{
        try {
            Picture pic = pictureService.get(code);
            String filename = pic.getUrl().replace("getimg?filename=", "");

            CommonOperation.getImage(filename, pictureSavePath, request, response);
        }catch (JsonException e){
            System.out.println(e.toJson());
        }
    }
}
