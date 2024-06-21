package com.nc13.springBoard.restController;

import com.nc13.springBoard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserRestController {
    @Autowired
    UserService userService;

    @GetMapping("validateUsername")
    public Map<String, Object> validateUserbane(String username){
        Map<String, Object> resultMap=new HashMap<>();
        boolean result= userService.validateUsername(username);
        System.out.println("username: " +  username);
        if (result){
            resultMap.put("result","success");
        } else {
            resultMap.put("result","fail");
        }

        //resultMap.put("message","Message From Rest Controller!");

        return resultMap;
    }
}
