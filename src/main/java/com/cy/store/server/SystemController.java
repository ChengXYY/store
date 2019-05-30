package com.cy.store.server;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.config.AuthCode;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Admin;
import com.cy.store.model.Admingroup;
import com.cy.store.model.Adminlog;
import com.cy.store.service.AdminService;
import com.cy.store.service.AdmingroupService;
import com.cy.store.service.AdminlogService;
import com.cy.store.config.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController extends AdminConfig {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdmingroupService admingroupService;
    @Autowired
    private AdminlogService adminlogService;

    @RequestMapping("/admin/list")
    public String adminList(HttpSession session, ModelMap model){
        Map<String, Object>filter = new HashMap<String, Object>();
        //只获取当前用户下及用户
        filter.put("parentid", session.getAttribute(adminId));
        filter.put("order", "id asc");
        List<Admin> list = adminService.getList(filter);

        int totalCount = list.size();
        model.addAttribute("list", list);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageTitle",listPageTitle+adminModuleTitle+systemTitle);

        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admin");
        return "/admin/admin_list";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/resetpwd/submit", produces = {"application/json;charset=UTF-8"})
    public JSONObject passwordReset(Integer id){

        try{
            return adminService.resetPassword(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping("/admin/add")
    public String adminAdd(HttpSession session, ModelMap model){
        List<Admingroup> list = admingroupService.getListAll(Integer.parseInt(session.getAttribute(adminId).toString()));
        model.addAttribute("list", list);
        model.addAttribute("pageTitle",addPageTitle+adminModuleTitle+systemTitle);
        return "/admin/admin_add";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/add/submit", produces = {"application/json;charset=UTF-8"})
    public JSONObject adminAdd(Admin admin, HttpSession session){
        if(admin.getName()==null || admin.getName().isEmpty()){
            admin.setName(admin.getAccount());
        }
        admin.setParentid(Integer.parseInt(session.getAttribute(adminId).toString()));

        try {
            return adminService.add(admin);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping("/admin/edit/{id}")
    public String adminEdit(@PathVariable("id") Integer id, HttpSession session, ModelMap model){
        Admin admin = adminService.get(id);
        if(admin == null){
            model.addAttribute("pageTitle","404 Error");
            return "error/404";
        }
        List<Admingroup> list = admingroupService.getListAll(Integer.parseInt(session.getAttribute(adminId).toString()));
        model.addAttribute("list", list);
        model.addAttribute("admin", admin);
        model.addAttribute("pageTitle",editPageTitle+adminModuleTitle+systemTitle);
        return "/admin/admin_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/edit/submit", method = RequestMethod.POST)
    public JSONObject editAdmin(Admin admin ){

        try {
            return adminService.edit(admin);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/admin/remove", method = RequestMethod.POST)
    public JSONObject removeAdmin(@RequestParam(value = "id", required = true)Integer id){

        try {
            return adminService.remove(id);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    // AdminGroup 处理
    @RequestMapping("/admingroup/list")
    public String admingroupList(HttpSession session, ModelMap model){
        List<Admingroup> list = admingroupService.getListAll(Integer.parseInt(session.getAttribute(adminId).toString()));
        model.addAttribute("list", list);

        int totalCount = list.size();
        model.addAttribute("pageTitle",listPageTitle+admingroupModuleTitle+systemTitle);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "admingroup");
        return "/admin/admingroup_list";
    }

    @RequestMapping("/admingroup/add")
    public String admingroupAdd(ModelMap model){
        return "/admin/admingroup_add";
    }

    @ResponseBody
    @RequestMapping("/admingroup/add/submit")
    public JSONObject admingroupAdd(Admingroup admingroup, HttpSession session){
        admingroup.setParentid(Integer.parseInt(session.getAttribute(adminId).toString()));
        try {
            return admingroupService.add(admingroup);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    @RequestMapping("/admingroup/edit/{id}")
    public String admingroupEdit(@PathVariable("id") Integer id, ModelMap model){

        Admingroup admingroup = admingroupService.get(id);
        model.addAttribute("item", admingroup);
        return "/admin/admingroup_edit";
    }

    @ResponseBody
    @RequestMapping("/admingroup/edit/submit")
    public JSONObject admingroupEdit(Admingroup admingroup){

        try {
            return admingroupService.edit(admingroup);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    //权限
    @RequestMapping("/admingroup/auth/{id}")
    public String admingrouAuth(@PathVariable("id")Integer id,
                                    HttpSession session,
                                    ModelMap model){
        try {
            //权限只能是当前用户权限的子集
            List<Map<String, Object>> list = AuthCode.listAuthCode();
            List<Map<String, Object>> list1 = list;
            String currentAuth = session.getAttribute(adminAuth).toString();
            for(int i=0; i<list.size(); i++){
                if(!currentAuth.contains(list.get(i).get("code").toString())){
                    //System.out.println(list.get(i).get("code").toString());
                    list.remove(i);
                    i--;
                }
            }
            Admingroup admingroup = admingroupService.get(id);
            String authStr = admingroup.getAuth();
            model.addAttribute("authlist", authStr);

            model.addAttribute("list", list);
            model.addAttribute("groupid", id);
            model.addAttribute("groupName", admingroup.getName());
            model.addAttribute("pageTitle",authPageTitle+admingroupModuleTitle+systemTitle);

            model.addAttribute("TopMenuFlag", "system");
            model.addAttribute("LeftMenuFlag", "admingroup");
            return "/admin/admingroup_auth";
        }catch (JsonException e){
            model.addAttribute("error", e.toJson());
            return "/error/common";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/admingroup/auth/save", method = RequestMethod.POST)
    public JSONObject authSave(Integer id, @RequestParam(value = "authcodes[]") String[] authcodes){
        try {
            return admingroupService.changeAuth(id, authcodes);
        }catch (JsonException e){
            return e.toJson();
        }
    }

    //log list
    @RequestMapping(value = "/adminlog/list", method = RequestMethod.GET)
    public String adminlogList(@RequestParam(value = "content", required = false)String content,
                                @RequestParam(value = "page", defaultValue = "1", required = false)Integer page,
                               HttpServletRequest request,
                               ModelMap model){
        Map<String, Object>filter = new HashMap<>();
        filter.put("order", "id desc");
        String currentUrl = request.getRequestURI();
        if(content!=null && !content.isEmpty()){
            filter.put("content",content);
            currentUrl += "?content="+content;
        }else{
            content = "";
        }
        if(page == null || page<1){
            page = 1;
        }
        int totalCount = adminlogService.getCount(filter);
        int pageCount = (int)Math.ceil(totalCount/pageSize);
        if(pageCount <1){
            pageCount = 1;
        }

        filter.put("page", (page-1)*pageSize);
        filter.put("pagesize", pageSize);
        List<Adminlog> list = adminlogService.getList(filter);

        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentUrl", currentUrl);

        model.addAttribute("list", list);
        model.addAttribute("content", content);

        model.addAttribute("pageTitle",listPageTitle+adminlogModuleTitle+systemTitle);
        model.addAttribute("TopMenuFlag", "system");
        model.addAttribute("LeftMenuFlag", "adminlog");
        return "/admin/adminlog_list";
    }
}
