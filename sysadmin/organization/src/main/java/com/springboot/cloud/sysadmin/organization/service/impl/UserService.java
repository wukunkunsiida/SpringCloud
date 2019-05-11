package com.springboot.cloud.sysadmin.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.cloud.sysadmin.organization.dao.UserMapper;
import com.springboot.cloud.sysadmin.organization.entity.param.UserQueryParam;
import com.springboot.cloud.sysadmin.organization.entity.po.User;
import com.springboot.cloud.sysadmin.organization.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public long add(User user) {
        return userMapper.insert(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#root.targetClass.name+'-'+#id")
    public void delete(long id) {
        userMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "user", key = "#root.targetClass.name+'-'+#user.id")
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    @Cacheable(value = "user", key = "#root.targetClass.name+'-'+#id")
    public User get(long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> query(UserQueryParam userQueryParam) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(null != userQueryParam.getCreatedTimeStart(), "created_time", userQueryParam.getCreatedTimeStart());
        queryWrapper.le(null != userQueryParam.getCreatedTimeEnd(), "created_time", userQueryParam.getCreatedTimeEnd());
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getName()), "name", userQueryParam.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getUsername()), "username", userQueryParam.getUsername());
        queryWrapper.eq(StringUtils.isNotBlank(userQueryParam.getMobile()), "mobile", userQueryParam.getMobile());
        return userMapper.selectList(queryWrapper);
    }
}