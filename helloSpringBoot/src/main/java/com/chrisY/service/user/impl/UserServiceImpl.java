package com.chrisY.service.user.impl;

import com.chrisY.dao.UserDao;
import com.chrisY.domain.user.User;
import com.chrisY.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-01 15:38
 * @description：
 */

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(User user) {
        return userDao.addUserInfo(user);
    }
}
