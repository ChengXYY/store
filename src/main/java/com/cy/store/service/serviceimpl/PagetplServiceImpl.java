package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.PagetplMapper;
import com.cy.store.model.Pagetpl;
import com.cy.store.service.PagetplService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagetplServiceImpl implements PagetplService {

    @Autowired
    private PagetplMapper pagetplMapper;

    @Override
    public JSONObject add(Pagetpl pagetpl) {
        if(pagetpl.getName() == null || pagetpl.getName().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        int rs = pagetplMapper.insertSelective(pagetpl);
        if(rs > 0){
            return CommonOperation.success(pagetpl.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Pagetpl pagetpl) {
        if(pagetpl.getName() == null || pagetpl.getName().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        get(pagetpl.getId());

        int rs = pagetplMapper.updateByPrimaryKeySelective(pagetpl);
        if(rs > 0){
            return CommonOperation.success(pagetpl.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs = pagetplMapper.deleteByPrimaryKey(id);
        if(rs > 0){
            return CommonOperation.success(id);
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(String ids) {
        if(ids == null || ids.isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        ids = ids.replace(" ", "");
        String[] idArr = ids.split(",");
        String msg = "";
        int success = 0;
        int count = 0;
        int fail = 0;
        for(String id : idArr){
            count ++;
            try {
                remove(Integer.parseInt(id));
                success++;
            }catch (JsonException e){
                msg += "ID"+id+"："+ e.getMsg()+ "。";
                fail++;
            }
        }
        msg = "成功删除："+success+"，失败："+fail+"。"+msg;
        if(fail > 0){
            return CommonOperation.fail(msg);
        }else {
            return CommonOperation.success(msg);
        }
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return pagetplMapper.countByFilter(filter);
    }

    @Override
    public List<Pagetpl> getList(Map<String, Object> filter) {
        return pagetplMapper.selectByFilter(filter);
    }

    @Override
    public JSONObject get(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        Pagetpl page = pagetplMapper.selectByPrimaryKey(id);
        if(page == null) throw JsonException.newInstance(ErrorCodes.ITEM_NOT_EXIST);
        return CommonOperation.success(page);
    }

    @Override
    public List<Map<String, Object>> getSelectList() {
        return pagetplMapper.selectListAll();
    }
}
