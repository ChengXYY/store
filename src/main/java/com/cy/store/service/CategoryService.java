package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    JSONObject add(Category category);

    JSONObject edit(Category category);

    JSONObject remove(Integer id);

    //batch delete
    JSONObject remove(String ids);

    List<Category> getList(Map<String, Object> filter);

    Integer getCount(Map<String, Object> filter);

    Category get(Integer id);

    Category get(String code);
}
