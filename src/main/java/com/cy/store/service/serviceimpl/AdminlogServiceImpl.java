package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.AdminlogMapper;
import com.cy.store.model.Adminlog;
import com.cy.store.service.AdminlogService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminlogServiceImpl implements AdminlogService {

    @Autowired
    private AdminlogMapper adminlogMapper;

    @Override
    public JSONObject add(String admin, String content) {
        if(admin.isEmpty() || content.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        Adminlog log = new Adminlog();
        log.setContent(content);
        log.setAdmin(admin);
        int rs =  adminlogMapper.insertSelective(log);
        if (rs >0)
            return CommonOperation.success();
        else
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
    }

    @Override
    public List<Adminlog> getList(Map<String, Object> filter) {
        return adminlogMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return adminlogMapper.countByFilter(filter);
    }
}
