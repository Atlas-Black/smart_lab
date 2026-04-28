package com.yiguan.smart_lab.controller;

import com.yiguan.smart_lab.common.Result;
import com.yiguan.smart_lab.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StrategyTestController {

    private final NotificationService notificationService;

    public StrategyTestController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping("/notify/{deviceId}")
    public Result<String> notifyTest(@PathVariable long deviceId){
        notificationService.notifyByDeviceValue(deviceId);
        return Result.success("通知策略执行成功");
    }
}
