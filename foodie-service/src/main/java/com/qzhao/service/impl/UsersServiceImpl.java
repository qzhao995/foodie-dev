package com.qzhao.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.qzhao.enums.Sex;
import com.qzhao.mapper.UsersMapper;
import com.qzhao.pojo.Users;
import com.qzhao.pojo.bo.UserBo;
import com.qzhao.service.UsersService;
import com.qzhao.utils.DateUtil;
import com.qzhao.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UsersServiceImpl implements UsersService {
    @Resource
    UsersMapper usersMapper;
    @Autowired
    Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        Object result = usersMapper.selectOneByExample(example);

        return   result == null ? false : true;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users SaveUsers(UserBo userBo){
        String id = sid.nextShort();
        Users users = new Users();

        BeanUtils.copyProperties(userBo,users);
        users.setId(id);

        try {
            users.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.setNickname(userBo.getUsername());
        users.setFace(USER_FACE);
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        usersMapper.insert(users);
        return users;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);

        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }

}
