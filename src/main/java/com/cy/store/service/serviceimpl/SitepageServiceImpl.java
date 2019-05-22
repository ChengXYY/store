package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.SitepageMapper;
import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SitepageServiceImpl implements SitepageService {

    @Autowired
    private SitepageMapper sitepageMapper;

    @Override
    public JSONObject add(Sitepage sitepage) {
        if(sitepage.getCode().isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Sitepage page = get(sitepage.getCode());
        if(page!=null) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);

        int rs = sitepageMapper.insertSelective(sitepage);
        if(rs > 0){
            return CommonOperation.success(sitepage.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Sitepage sitepage) {
        if(sitepage.getId() == null || sitepage.getId() < 1) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        if(sitepage.getCode().isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);

        String content = sitepage.getContent();
        if(!content.isEmpty()){
            content = content.replace("<br />", "");
            content = content.replace("<br>", "");
            content = content.replace("<em>", "");
            sitepage.setContent(content);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", sitepage.getId());
        data.put("code", sitepage.getCode());
        data.put("title", sitepage.getTitle());
        data.put("content", sitepage.getContent());
        int rs = sitepageMapper.updateByPrimaryKeySelective(data);

        if(rs > 0){
            return CommonOperation.success(sitepage.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs = sitepageMapper.deleteByPrimaryKey(id);

        if(rs > 0){
            return CommonOperation.success(id);
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public List<Sitepage> getList(Map<String, Object> filter) {
         return sitepageMapper.selectByFilter(filter);
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return sitepageMapper.countByFilter(filter);
    }

    @Override
    public Sitepage get(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        return sitepageMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sitepage get(String code) {
        if(code ==null || code.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        return sitepageMapper.selectByCode(code);
    }
}
