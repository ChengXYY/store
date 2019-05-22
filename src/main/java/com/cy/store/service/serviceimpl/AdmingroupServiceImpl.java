package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Admingroup> getListAll(Integer parentid) {
        return admingroupMapper.selectAll(parentid);
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
        Map<String, Object> data = new HashMap<>();
        data.put("id", admingroup.getId());
        data.put("name", admingroup.getName());
        data.put("auth", admingroup.getAuth());
        data.put("sort",admingroup.getSort());
        data.put("parentid", admingroup.getParentid());
        int rs = admingroupMapper.updateByPrimaryKeySelective(data);
        if(rs > 0){
            return CommonOperation.success(admingroup.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
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
        Map<String, Object> group = new HashMap<>();
        group.put("id", id);
        group.put("auth", authStr);
        int rs = admingroupMapper.updateByPrimaryKeySelective(group);
        if(rs > 0){
            return CommonOperation.success(id);
        }else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }


}
