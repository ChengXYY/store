package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    JSONObject add(Article article);

    JSONObject edit(Article article);

    JSONObject remove(Integer id);

    List<Article> getList(Map<String, Object> filter);

    Integer getCount(Map<String, Object> filter);

    Article get(Integer id);

    Article get(String code);
}
