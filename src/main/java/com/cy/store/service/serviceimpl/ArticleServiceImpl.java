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
        if(!CommonOperation.checkId(article.getId())) throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
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
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);
        int rs = articleMapper.deleteByPrimaryKey(id);

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
    public List<Article> getList(Map<String, Object> filter) {
        return articleMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return articleMapper.countByFilter(filter);
    }

    @Override
    public Article get(Integer id) {
        if(!CommonOperation.checkId(id))throw JsonException.newInstance(ErrorCodes.ID_NOT_LEGAL);

        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Article get(String code) {
        if(code == null || code.isEmpty())throw JsonException.newInstance(ErrorCodes.PARAM_NOT_EMPTY);

        return articleMapper.selectByCode(code);
    }
}
