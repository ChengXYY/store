package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.utils.CommonOperation;
import com.cy.store.config.AuthCode;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.AdminMapper;
import com.cy.store.model.Admin;
import com.cy.store.service.AdminService;
import com.cy.store.config.AdminConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl extends AdminConfig implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JSONObject add(Admin admin){
        if(admin.getAccount().isEmpty() ||
                admin.getGroupid().toString().equals("0") ||
                admin.getPassword().isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        if(admin.getName()== null || admin.getName().isEmpty()){
            admin.setName(admin.getAccount());
        }
        String pwd = admin.getPassword();
        Map<String,Object>pwdArr = CommonOperation.encodeStr(pwd);
        admin.setSalt(pwdArr.get("salt").toString());
        admin.setPassword(pwdArr.get("newstr").toString());
        int rs = adminMapper.insertSelective(admin);
        if(rs > 0){
            return CommonOperation.success(admin.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Admin admin) {
        if(admin.getId()==null || admin.getId()<1 )throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        int rs =  adminMapper.updateByPrimaryKeySelective(admin);
        if(rs > 0){
            return CommonOperation.success(admin.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs =  adminMapper.deleteByPrimaryKey(id);
        if(rs > 0)
            return CommonOperation.success(id);
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public Admin get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        Admin admin = adminMapper.selectByPrimaryKey(id);
        if(admin == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admin;
    }

    @Override
    public Admin get(String account) {
        if(account.isEmpty()) throw  JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        Admin admin = adminMapper.selectByAccount(account);
        if(admin == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admin;
    }

    @Override
    public List<Admin> getList(Map<String, Object> filter) {
        return adminMapper.selectByFilter(filter);
    }

    @Override
    public JSONObject resetPassword(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        Admin admin = get(id);
        if(admin == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        Map<String,Object>pwdArr = CommonOperation.encodeStr("123456");
        Admin adminNew = new Admin();
        adminNew.setId(id);
        adminNew.setSalt(pwdArr.get("salt").toString());
        adminNew.setPassword(pwdArr.get("newstr").toString());
        JSONObject rs = edit(adminNew);
        if(rs.get("code").equals("0"))
            return CommonOperation.success(id);
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public void login(String account, String password, String vercode, HttpSession session) {
        if(session.getAttribute(adminSession) != null) return;
        if(account.isEmpty() || password.isEmpty() || vercode.isEmpty()) throw  JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //验证码
        if(session.getAttribute(verCode).toString().isEmpty()) throw JsonException.newInstance(ErrorCodes.VERCODE_NOT_EMPTY);
        if(!session.getAttribute(verCode).toString().equals(vercode)) throw JsonException.newInstance(ErrorCodes.VERCODE_IS_WRONG);
        if(isSystem(account, password, session)) return;

        Admin admin = get(account);
        String varifyPwd = CommonOperation.encodeStr(password, admin.getSalt());
        if(!varifyPwd.equals(admin.getPassword())) throw JsonException.newInstance(ErrorCodes.PASSWORD_IS_WRONG);
        String sessionStr = CommonOperation.encodeStr(admin.getId().toString(), admin.getAccount());
        session.setAttribute(adminSession, sessionStr);
        session.setAttribute(adminAccount, admin.getAccount());
        session.setAttribute(adminAuth, admin.getAdmingroup().getAuth());
        session.setAttribute(adminGroup, admin.getAdmingroup().getId());
        session.setAttribute(adminId, admin.getId());
    }

    private Boolean isSystem(String account, String password, HttpSession session){
        if(account.equals(sysAccount) && password.equals(sysPassword)){
            String sessionStr = CommonOperation.encodeStr("0", account);
            session.setAttribute(adminSession, sessionStr);
            session.setAttribute(adminAccount, account);
            String auth = AuthCode.getAuthString();
            session.setAttribute(adminAuth, auth);
            session.setAttribute(adminGroup, "0");
            session.setAttribute(adminId, "0");
            return true;
        }
        return  false;
    }

    @Override
    public void editPassword(String oldpwd, String newpwd, String repwd, HttpSession session) {
        if(oldpwd.isEmpty() || newpwd.isEmpty() || repwd.isEmpty())throw  JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        if(!newpwd.equals(repwd))throw JsonException.newInstance(ErrorCodes.PASSWORD_NOT_SAME);
        Admin admin = get(session.getAttribute(adminAccount).toString());
        if(admin == null)throw  JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        String varifyPwd = CommonOperation.encodeStr(oldpwd, admin.getSalt());
        if(!varifyPwd.equals(admin.getPassword())) throw JsonException.newInstance(ErrorCodes.PASSWORD_IS_WRONG);
        Map<String, Object> pwdArr = CommonOperation.encodeStr(newpwd);
        Admin adminNew = new Admin();
        adminNew.setId(admin.getId());
        adminNew.setSalt(pwdArr.get("salt").toString());
        adminNew.setPassword(pwdArr.get("newstr").toString());
        JSONObject rs = edit(adminNew);
        if(rs.get("code").equals("0"))
            return;
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public Admin getCurrentUser() {
        ServletRequestAttributes attributes =   (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session= request.getSession();
        if(session.getAttribute(adminAccount) == null) throw JsonException.newInstance(ErrorCodes.UN_LOGIN);
        return get(session.getAttribute(adminAccount).toString());
    }
}
