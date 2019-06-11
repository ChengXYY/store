package com.cy.store.mapper;

import com.cy.store.model.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int countByFilter(Map<String, Object> filter);

    List<Category> selectByFilter(Map<String, Object> filter);

    Category selectByCode(@Param("code")String code);
}