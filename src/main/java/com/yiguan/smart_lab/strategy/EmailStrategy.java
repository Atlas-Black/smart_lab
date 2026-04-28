package com.yiguan.smart_lab.strategy;

import com.yiguan.smart_lab.model.LabDevice;
import org.springframework.stereotype.Component;

@Component("HIGH")
public class EmailStrategy implements MessageStrategy{

    @Override
    public void sendMessage(LabDevice device){
        System.out.println("发送邮件通知管理员，设备：" + device.getDeviceName());
    }
}

