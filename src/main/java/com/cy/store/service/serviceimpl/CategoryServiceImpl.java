package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.CategoryMapper;
import com.cy.store.model.Category;
import com.cy.store.service.CategoryService;
import com.cy.store.utils.CommonOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public JSONObject add(Category category) {
        if(StringUtils.isBlank(category.getCode()) || StringUtils.isBlank(category.getName()))throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Category category1 = get(category.getCode());
        if(category1 != null)throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        int rs = categoryMapper.insertSelective(category);
        if(rs > 0){
            return CommonOperation.success(category.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Category category) {
        if(!CommonOperation.checkId(category.getId())) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        if(StringUtils.isNotBlank(category.getCode())){
            Category category1 = get(category.getCode());
            if(!category1.getId().equals(category.getId()))throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        }
        int rs = categoryMapper.updateByPrimaryKeySelective(category);
        if(rs >= 0){
            return CommonOperation.success(category.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        //判断是否存在
        //删除图片
        return null;
    }

    @Override
    public JSONObject remove(String ids) {
        return null;
    }

    @Override
    public List<Category> getList(Map<String, Object> filter) {
        return categoryMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return categoryMapper.countByFilter(filter);
    }

    @Override
    public Category get(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Category get(String code) {
        if(StringUtils.isBlank(code))throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        return categoryMapper.selectByCode(code);
    }
}
