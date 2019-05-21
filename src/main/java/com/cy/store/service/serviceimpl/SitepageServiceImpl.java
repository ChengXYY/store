package com.cy.store.service.serviceimpl;

import com.cy.store.model.Sitepage;
import com.cy.store.service.SitepageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SitepageServiceImpl implements SitepageService {
    @Override
    public int add(Sitepage sitepage) {
        return 0;
    }

    @Override
    public int edit(Map<String, Object> sitepage) {
        return 0;
    }

    @Override
    public int remove(Integer id) {
        return 0;
    }

    @Override
    public List<Sitepage> getList(Map<String, Object> filter) {
        return null;
    }

    @Override
    public int getCount(Map<String, Object> filter) {
        return 0;
    }

    @Override
    public Sitepage get(Integer id) {
        return null;
    }
}
