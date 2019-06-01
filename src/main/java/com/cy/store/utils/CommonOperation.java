package com.cy.store.utils;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.AdminConfig;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonOperation extends AdminConfig {
    public static Map<String, Object> encodeStr(String str){
        Map<String, Object> rs = new HashMap<String, Object>();
        //get salt
        String salt = UUID.randomUUID().toString();
        rs.put("salt", salt);
        //md5
        String str1 = DigestUtils.md5DigestAsHex((str+salt).getBytes());
        rs.put("newstr",str1);
        return rs;
    }

    public static String encodeStr(String str, String salt){
        String str1 = DigestUtils.md5DigestAsHex((str+salt).getBytes());
        return str1;
    }

    public static boolean checkId(Integer id){
        if(id == null) return false;
        if(id.toString().isEmpty()) return false;
        if(id.intValue() < 1)return false;
        return true;
    }

    public static boolean checkEmail(String email){
        if(email == null || email.isEmpty())return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
        if (m.matches())
            return true;
        else
            return false;
    }

    //上传文件
    public static JSONObject uploadFile(MultipartFile file, String type, String myFileName){
        JSONObject rs = new JSONObject();
        if(file == null || !fileType.contains(type))throw JsonException.newInstance(ErrorCodes.FILE_NOT_EXSIT);

        String fileName = file.getOriginalFilename();
        String newFileName = myFileName;
        if(myFileName == null || myFileName.isEmpty()){
            newFileName = System.currentTimeMillis() + "-" +fileName;
        }
        newFileName = type + "-" + newFileName;
        String savePath = baseSavePath+type;

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        int size = (int)file.getSize();
        size = (int)Math.ceil(size/1024);
        if(size <= 0)throw JsonException.newInstance(ErrorCodes.FILE_UPLOAD_FAILED);
        String destDir = savePath + "/" + newFileName;
        File dest  = new File(destDir);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
            rs.put("code", 0);
            rs.put("msg", "上传成功");
            rs.put("size", size);
            rs.put("filename", fileName);
            rs.put("realname", newFileName);
            rs.put("ext", ext);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
           throw JsonException.newInstance(ErrorCodes.FILE_UPLOAD_FAILED);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw JsonException.newInstance(ErrorCodes.FILE_WRITE_FAILED);
        }
        return rs;
    }

    public static JSONObject uploadFile(MultipartFile file, String type){
        return uploadFile(file, type, null);
    }

    //查看图片
    public static void getImage(String filename,
                                HttpServletRequest request,
                                HttpServletResponse response)throws IOException {

        if (filename != null || filename.isEmpty()) {
            FileInputStream is = null;

            String type = filename.substring(0, filename.indexOf('-'));
            if(!fileType.contains(type))return;
            String path = baseSavePath+type+"/"+filename;
            File file = new File(path);
            try {
                is = new FileInputStream(file);
                int i = is.available();
                byte data[] = new byte[i];
                is.read(data);
                is.close();
                response.setContentType("image/jpeg");
                OutputStream toClient = response.getOutputStream();
                toClient.write(data);
                toClient.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    public static JSONObject removeFile(String fileName){
        JSONObject rs = new JSONObject();
        if(fileName == null || fileName.isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);

        String type = fileName.substring(0, fileName.indexOf('-')+1);
        if(!fileType.contains(type))throw JsonException.newInstance(ErrorCodes.PATH_IS_WRONG);
        String path = baseSavePath+type+"/"+fileName;

        File file = new File(path);
        if(file.exists() && file.isFile()){
            if(file.delete()){
                return success();
            }else {
                throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
            }
        }else{
            throw JsonException.newInstance(ErrorCodes.FILE_NOT_EXSIT);
        }
    }

    public static JSONObject success(Integer id){
        JSONObject rs = new JSONObject();
        rs.put("retCode", 0);
        rs.put("retMsg", "操作成功");
        rs.put("id", id);
        return  rs;
    }
    public static JSONObject success(){
        JSONObject rs = new JSONObject();
        rs.put("retCode", 0);
        rs.put("retMsg", "操作成功");
        return  rs;
    }

    public static JSONObject success(Object obj){
        JSONObject rs = new JSONObject();
        rs.put("retCode", 0);
        rs.put("retMsg", "操作成功");

        Map<String, Object> data = BeanMap.create(obj);
        rs.putAll(data);
        return rs;
    }

    public static JSONObject obj2Json(Object object){
        JSONObject rs = new JSONObject();

        Map<String, Object> data = BeanMap.create(object);
        rs.putAll(data);
        return rs;
    }

}
