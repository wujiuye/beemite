package com.wujiuye.ipweb.service.impl;

import com.wujiuye.ipweb.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public Map<String, Object> queryUser(String username, Integer age) {
        Map<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("age",age);
        return result;
    }

}
