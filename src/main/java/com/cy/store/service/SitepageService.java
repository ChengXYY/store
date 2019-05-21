package com.cy.store.service;

import com.cy.store.model.Sitepage;

import java.util.List;
import java.util.Map;

public interface SitepageService {

    int add(Sitepage sitepage);

    int edit(Map<String, Object> sitepage);

    int remove(Integer id);

    List<Sitepage> getList(Map<String, Object> filter);

    int getCount(Map<String, Object> filter);

    Sitepage get(Integer id);
}
