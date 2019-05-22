package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Pagetpl;

import java.util.List;
import java.util.Map;

public interface PagetplService {
    JSONObject add(Pagetpl pagetpl);

    JSONObject edit(Pagetpl pagetpl);

    JSONObject remove(Integer id);

    int getCount(Map<String, Object> filter);

    List<Pagetpl> getList(Map<String, Object> filter);

    JSONObject get(Integer id);

    List<Map<String, Object>> getSelectList();
}
