package com.immortal.jinfeng.service;

import com.immortal.jinfeng.access.UserinfoMapper;
import com.immortal.jinfeng.entity.Userinfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//v2版本在service层应该面向接口编程
@Service
@Transactional
public class UserinfoService {

    @Resource
    UserinfoMapper userinfoMapper;

    //验证用户名是否存在
    @Transactional(readOnly = true)
    public boolean checkName(String username){
        return userinfoMapper.checkName(username);//存在返回true，反之
    }

    //查找该用户id的用户是否存在
    @Transactional(readOnly = true)
    public boolean checkUid(Long uid){
        return userinfoMapper.checkUid(uid);
    }

    //注册
    public int reg(String username, String password){

        if(this.checkName(username)){
            return 2;//用户已存在，不允许注册
        }

        Userinfo userinfo = new Userinfo();
        userinfo.setUsername(username);
        userinfo.setPassword(password);
        userinfo.setFileNames("默认头像");
        int result = userinfoMapper.insertSelective(userinfo);

        if(result == 1){
            return 1;//注册成功
        }else{
            return 0;//注册失败
        }

    }

    //工具id获取用户的全部信息
    @Transactional(readOnly = true)
    public Userinfo getUserinfoByUid(Long uid){
        return userinfoMapper.selectByPrimaryKey(uid);
    }

//    根据用户名获取用户的全部信息
    @Transactional(readOnly = true)
    public Userinfo getUserinfoByUsername(String username){
        return userinfoMapper.selectByUsername(username);
    }

    //根据uid查找并修改用户信息
    public int updateUserinfoByUid(Userinfo userinfo){
        return userinfoMapper.updateByPrimaryKeySelective(userinfo);
    }

    //查询token是否存在
    @Transactional(readOnly = true)
    public boolean verify(String token){
        return userinfoMapper.verify(token);
    }

    //分页模糊查询多个用户
    @Transactional(readOnly = true)
    public List<Userinfo> getUserinfoList(int page, int pagesize, String sex, String name){
        int state = (page - 1) * pagesize;
        Map param = new HashMap();

        param.put("state", state);
        param.put("num",pagesize);
        param.put("sex",sex);
        param.put("name",name);

        return userinfoMapper.getUserinfoList(param);
    }

    //查询一共注册了多少个用户
    @Transactional(readOnly = true)
    public int getUserAmount(){
        return userinfoMapper.getUserAmount();
    }

    //添加一个用户
    public int addUser(Userinfo userinfo){
        if(this.checkName(userinfo.getUsername())){
            return 2;//用户已存在，不允许添加
        }
        return userinfoMapper.insertSelective(userinfo);//1则代表正确添加，0则代表添加失败
    }

    //根据uid删除一个用户
    public int del(Long uid){
        return userinfoMapper.deleteByPrimaryKey(uid);
    }
}