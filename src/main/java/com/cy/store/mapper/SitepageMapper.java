package com.cy.store.mapper;

import com.cy.store.model.Sitepage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SitepageMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Sitepage record);

    Sitepage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sitepage record);

    List<Sitepage> selectByFilter(Map<String, Object> filter);

    int countByFilter(Map<String, Object> filter);

    Sitepage selectByCode(@Param(value = "code")String code);
}