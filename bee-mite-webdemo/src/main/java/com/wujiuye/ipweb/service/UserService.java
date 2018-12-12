package com.wujiuye.ipweb.service;

import java.util.Map;

public interface UserService {

    Map<String,Object> queryUser(String username, Integer age);

}
