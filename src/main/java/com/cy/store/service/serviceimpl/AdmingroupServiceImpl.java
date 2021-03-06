package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.mapper.AdminMapper;
import com.cy.store.model.Admin;
import com.cy.store.model.Sitepage;
import com.cy.store.utils.CommonOperation;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.AdmingroupMapper;
import com.cy.store.model.Admingroup;
import com.cy.store.service.AdmingroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdmingroupServiceImpl implements AdmingroupService {

    @Autowired
    private AdmingroupMapper admingroupMapper;

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public List<Admingroup> getListAll(Integer parentid) {
        return admingroupMapper.selectAll(parentid);
    }

    @Override
    public Integer countAll(Integer parentid) {
        return getListAll(parentid).size();
    }

    @Override
    public JSONObject add(Admingroup admingroup) {
        if(admingroup.getName().isEmpty() || !CommonOperation.checkId(admingroup.getSort())) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        int rs = admingroupMapper.insertSelective(admingroup);
        if(rs>0) {
            return CommonOperation.success(admingroup.getId());
        }else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public JSONObject edit(Admingroup admingroup) {
        if(admingroup.getId()==null || admingroup.getId()<1 )throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        int rs = admingroupMapper.updateByPrimaryKeySelective(admingroup);
        if(rs > 0){
            return CommonOperation.success(admingroup.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        // 判断该组是否存在
        Admingroup group = get(id);
        if(group == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        // 先判断组内是否有成员
        Map<String, Object> filter = new HashMap<>();
        filter.put("groupid", id);
        List<Admin> members = adminMapper.selectByFilter(filter);
        if(members.size() >0) throw JsonException.newInstance(ErrorCodes.GROUP_NOT_EMPTY);

        int rs = admingroupMapper.deleteByPrimaryKey(id);

        if(rs > 0){
            return CommonOperation.success(id);
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public Admingroup get(Integer id) {
        if(!CommonOperation.checkId(id)) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        Admingroup admingroup = admingroupMapper.selectByPrimaryKey(id);
        if(admingroup == null)throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return admingroup;
    }

    @Override
    public JSONObject changeAuth(Integer id, String[] auths) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        Admingroup admingroup = get(id);

        String authStr = "";
        if(auths.length>0){
            for (String code : auths){
                authStr += code+"|";
            }
            authStr.substring(0,authStr.length()-1);
        }
        admingroup.setAuth(authStr);
        int rs = admingroupMapper.updateByPrimaryKeySelective(admingroup);
        if(rs > 0){
            return CommonOperation.success(id);
        }else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }


}
