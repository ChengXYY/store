package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.SitepageMapper;
import com.cy.store.model.Sitepage;
import com.cy.store.service.PagetplService;
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

    @Autowired
    private PagetplService pagetplService;

    @Override
    public JSONObject add(Sitepage sitepage) {
        if((!CommonOperation.checkId(sitepage.getTplid()) && (sitepage.getContent()!=null || !sitepage.getContent().isEmpty()))
                || sitepage.getCode().isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        if(sitepage.getTplid() > 0){
            pagetplService.get(sitepage.getTplid());
        }
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
        if(!CommonOperation.checkId(sitepage.getId())) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        if((sitepage.getTplid() == 0 && (sitepage.getContent()!=null || !sitepage.getContent().isEmpty()))
            || sitepage.getCode().isEmpty()) throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        if(sitepage.getTplid() > 0){
            pagetplService.get(sitepage.getTplid());
        }
        //判断重复
        Sitepage page = get(sitepage.getCode());
        if(page!=null && page.getId()!=sitepage.getId()) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);

        int rs = sitepageMapper.updateByPrimaryKeySelective(sitepage);

        if(rs > 0){
            return CommonOperation.success(sitepage.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs = sitepageMapper.deleteByPrimaryKey(id);

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
    public List<Sitepage> getList(Map<String, Object> filter) {
         return sitepageMapper.selectByFilter(filter);
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return sitepageMapper.countByFilter(filter);
    }

    @Override
    public Sitepage get(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        return sitepageMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sitepage get(String code) {
        if(code ==null || code.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        return sitepageMapper.selectByCode(code);
    }
}
