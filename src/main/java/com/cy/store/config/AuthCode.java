package com.cy.store.config;

import java.util.*;

public enum AuthCode {

    Menu_System(9001, "菜单查看权限-系统设置"),
    Menu_Index(8001,"菜单查看权限-首页"),

    //一级菜单： topmenu
    Menu_Sitepage(1001,"菜单查看权限-网页配置"),
    Menu_FileManager(1002,"菜单查看权限-资料管理"),
    Menu_Blog(1003,"菜单查看权限-产品管理"),

    //二级菜单：leftmenu
    Sys_Admin(2901, "系统权限-管理员管理"),
    Sys_AdminGroup(2902, "系统权限-管理员组管理"),
    Sys_AdminAuth(2903, "系统权限-管理员权限配置");

    private Integer code;
    private String intro;

    AuthCode(Integer code, String intro){
        this.code = code;
        this.intro = intro;
    }

    public Integer getCode(){
        return this.code;
    }
    public String getIntro(){
        return this.intro;
    }
    public static AuthCode fromAuthCode(Integer code){
        for (AuthCode codes : AuthCode.values()) {
            if (Objects.equals(code,codes.getCode())) {
                return codes;
            }
        }
        return null;
    }

    public static List<Map<String, Object>> listAuthCode(){
        List<Map<String, Object>> result = new ArrayList<>();
        for (AuthCode codes : AuthCode.values()) {
            Map<String, Object> item = new HashMap<>();
            item.put("code", codes.getCode());
            item.put("intro", codes.getIntro());
            result.add(item);
        }
        return result;
    }

    public static String getAuthString(){
        String rs = "";
        for (AuthCode codes : AuthCode.values()) {
            rs += codes.getCode()+"|";
        }
        rs.substring(0, rs.length()-1);
        return  rs;
    }
}
