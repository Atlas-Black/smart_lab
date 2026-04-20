package com.yiguan.smart_lab.controller;

import com.yiguan.smart_lab.common.Result;
import com.yiguan.smart_lab.context.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/login")
    public Result<Long> testLogin(){
        return Result.success(UserContext.getUserId());
    }
}
