package com.wujiuye.beemite.test;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl {

    public Map<String, Object> queryMap(String username,Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("age", age);
//        int n = 1/0;
        return map;
    }

}
