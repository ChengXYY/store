package com.cy.store.service;

import com.cy.store.model.Adminlog;

import java.util.List;
import java.util.Map;

public interface AdminlogService {
    //add
    int add(String admin, String content);

    //get list
    List<Adminlog> getList(Map<String, Object> filter);

    //get count
    Integer getCount(Map<String, Object> filter);
}
