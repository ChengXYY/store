package com.cy.store.mapper;

import com.cy.store.model.Admingroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdmingroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Admingroup record);

    Admingroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admingroup record);

    List<Admingroup> selectAll(@Param("parentid") Integer parentid);
}