package com.yiguan.smart_lab.controller;

import com.yiguan.smart_lab.common.Result;
import com.yiguan.smart_lab.dto.LoginDTO;
import com.yiguan.smart_lab.service.SysUserService;
import com.yiguan.smart_lab.vo.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.ResolutionSyntax;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService){
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO){
        return Result.success(sysUserService.login(loginDTO));
    }
}
