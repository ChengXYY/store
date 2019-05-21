package com.cy.store.common;

import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ImageController {

    @Value("${file.sitepage-image-path}")
    private String sitePageImg;

    @RequestMapping(value = "/sitepage/getimg", method = RequestMethod.GET)
    public void getImage(@RequestParam(value = "filename") String filename,
                         HttpServletRequest request, HttpServletResponse response)throws IOException {
        CommonOperation.getImage(filename, sitePageImg, request, response);
    }
}
