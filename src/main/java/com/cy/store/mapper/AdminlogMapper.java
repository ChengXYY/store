package com.cy.store.mapper;

import com.cy.store.model.Adminlog;

import java.util.List;
import java.util.Map;

public interface AdminlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Adminlog record);

    Adminlog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Adminlog record);

    List<Adminlog> selectByFilter(Map<String, Object> filter);

    Integer countByFilter(Map<String, Object> filter);
}