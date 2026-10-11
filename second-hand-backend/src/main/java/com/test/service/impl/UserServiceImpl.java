package com.test.service.impl;

import com.test.entity.User;
import com.test.mapper.UserMapper;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getStatus() == 1 && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean register(User user) {
        User exist = userMapper.findByUsername(user.getUsername());
        if (exist != null) {
            return false;
        }
        user.setPassword(user.getPassword());  // 直接存明文
        user.setRole("user");
        user.setStatus(1);
        return userMapper.insert(user) > 0;
    }

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean updateStatus(Integer id, Integer status) {
        return userMapper.updateStatus(id, status) > 0;
    }
}
