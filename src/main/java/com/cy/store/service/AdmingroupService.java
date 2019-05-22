package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Admingroup;

import java.util.List;
import java.util.Map;

public interface AdmingroupService {
    //list all
    List<Admingroup> getListAll(Integer parentid);

    //add
    JSONObject add(Admingroup admingroup);

    //edit
    JSONObject edit(Admingroup admingroup);

    //delete
    JSONObject remove(Integer id);

    //get
    Admingroup get(Integer id);

    //auth
    JSONObject changeAuth(Integer id, String[] auths);

}
