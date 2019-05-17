package com.cy.store.mapper;

import com.cy.store.model.Sitepage;

public interface SitepageMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Sitepage record);

    Sitepage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sitepage record);

    int updateByPrimaryKey(Sitepage record);
}