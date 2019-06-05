package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.exception.JsonException;
import com.cy.store.model.Sitepage;

import java.util.List;
import java.util.Map;

public interface SitepageService {

    JSONObject add(Sitepage sitepage);

    JSONObject edit(Sitepage sitepage);

    JSONObject remove(Integer id);

    JSONObject remove(String ids);

    List<Sitepage> getList(Map<String, Object> filter);

    int getCount(Map<String, Object> filter);

    Sitepage get(Integer id);

    Sitepage get(String code);
}
