package com.cy.store.mapper;

import com.cy.store.model.UserRecord;

public interface UserRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(UserRecord record);

    UserRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRecord record);
}