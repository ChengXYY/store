package com.cy.store.mapper;

import com.cy.store.model.Pagetpl;

import java.util.List;
import java.util.Map;

public interface PagetplMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Pagetpl record);

    Pagetpl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map<String, Object> record);

    List<Pagetpl> selectByFilter(Map<String, Object> filter);

    int countByFilter(Map<String, Object> filter);

    List<Map<String, Object>> selectListAll();
}