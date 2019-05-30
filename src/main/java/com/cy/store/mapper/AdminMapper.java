package com.cy.store.mapper;

import com.cy.store.model.Admin;

import java.util.List;
import java.util.Map;

public interface AdminMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Admin admin);

    Admin selectByPrimaryKey(Integer id);

    Admin selectByAccount(String account);

    List<Admin> selectByFilter(Map<String, Object> filter);

    int updateByPrimaryKeySelective(Admin admin);
}