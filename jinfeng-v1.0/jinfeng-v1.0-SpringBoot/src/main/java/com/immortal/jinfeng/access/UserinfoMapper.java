package com.immortal.jinfeng.access;

import com.immortal.jinfeng.entity.Userinfo;

import java.util.List;
import java.util.Map;

public interface UserinfoMapper {

    //验证用户名是否存在
    Boolean checkName(String username);

    //根据用户名获取用户的全部信息
    Userinfo selectByUsername(String username);

    //查询token是否存在
    Boolean verify(String token);

    //分页模糊查询多个用户
    List<Userinfo> getUserinfoList(Map data);

    //查询一共有多少条数据
    Integer getUserAmount();

    //查询是否存在该id的用户
    Boolean checkUid(Long uid);

    int deleteByPrimaryKey(Long uid);

    int insert(Userinfo record);

    int insertSelective(Userinfo record);

    Userinfo selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(Userinfo record);

    int updateByPrimaryKey(Userinfo record);
}