package com.cy.store.service.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.ArticleMapper;
import com.cy.store.model.Article;
import com.cy.store.service.ArticleService;
import com.cy.store.utils.CommonOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public JSONObject add(Article article) {
        if(article.getCode().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Article art = get(article.getCode());
        if(art!=null) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        int rs = articleMapper.insertSelective(article);
        if(rs > 0){
            return CommonOperation.success(article.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject edit(Article article) {
        if(article.getId() == null || article.getId() < 1) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        if(article.getCode().isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);
        //判断重复
        Article art = get(article.getCode());
        if(art!=null && art.getId()!=article.getId()) throw JsonException.newInstance(ErrorCodes.CODE_REPEATED);
        int rs = articleMapper.updateByPrimaryKeySelective(article);
        if(rs > 0){
            return CommonOperation.success(article.getId());
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public JSONObject remove(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs = articleMapper.deleteByPrimaryKey(id);

        if(rs > 0){
            return CommonOperation.success(id);
        }else {
            throw JsonException.newInstance(ErrorCodes.DATA_OP_FAILED);
        }
    }

    @Override
    public List<Article> getList(Map<String, Object> filter) {
        return articleMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return articleMapper.countByFilter(filter);
    }

    @Override
    public Article get(Integer id) {
        if(id == null || id <1)throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Article get(String code) {
        if(code == null || code.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);

        return articleMapper.selectByCode(code);
    }
}
