package com.chrisY.web;

import com.chrisY.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.chrisY.domain.user.User;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-01-24 16:25
 * @description：hello world!
 */


@RestController
@RequestMapping(value = "/hello")
public class HelloController {
    @Autowired
    private UserService userService;

    @ResponseBody

    @PostMapping("/add")
    public int addUser(User user)
    {
        System.out.println(user);
        return  userService.addUser(user);
    }

//    @PostMapping("/test")
//    public void test()
//    {
//        System.out.println(user);
//    }
}
