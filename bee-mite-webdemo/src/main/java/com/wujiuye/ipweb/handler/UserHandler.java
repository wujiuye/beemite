package com.wujiuye.ipweb.handler;

import com.wujiuye.ipweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserHandler {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}/{age}")
    public Map<String,Object> queryUser(
            @PathVariable("username")String username,
            @PathVariable("age") Integer age
    ){
        return userService.queryUser(username,age);
    }

}
