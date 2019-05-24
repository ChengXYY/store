package com.cy.store.service;

import com.alibaba.fastjson.JSONObject;
import com.cy.store.model.Picture;

import java.util.List;
import java.util.Map;

public interface PictureService {

    JSONObject add(Picture picture);

    JSONObject edit(Picture picture);

    JSONObject remove(Integer id);

    List<Picture> getList(Map<String, Object> filter);

    Integer getCount(Map<String, Object> filter);

    Picture get(Integer id);

    Picture get(String code);
}
