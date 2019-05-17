package com.cy.store.service.serviceimpl;

import com.cy.store.exception.ErrorCodes;
import com.cy.store.exception.JsonException;
import com.cy.store.mapper.AdminlogMapper;
import com.cy.store.model.Adminlog;
import com.cy.store.service.AdminlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminlogServiceImpl implements AdminlogService {

    @Autowired
    private AdminlogMapper adminlogMapper;

    @Override
    public int add(String admin, String content) {
        if(admin.isEmpty() || content.isEmpty())throw JsonException.newInstance(ErrorCodes.IS_NOT_EMPTY);
        Adminlog log = new Adminlog();
        log.setContent(content);
        log.setAdmin(admin);
        return adminlogMapper.insertSelective(log);
    }

    @Override
    public List<Adminlog> getList(Map<String, Object> filter) {
        return adminlogMapper.selectByFilter(filter);
    }

    @Override
    public Integer getCount(Map<String, Object> filter) {
        return adminlogMapper.countByFilter(filter);
    }
}
