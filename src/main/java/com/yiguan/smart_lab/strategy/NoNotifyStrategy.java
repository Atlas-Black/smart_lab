package com.yiguan.smart_lab.strategy;

import com.yiguan.smart_lab.model.LabDevice;
import org.springframework.stereotype.Component;

@Component("LOW")
public class NoNotifyStrategy implements MessageStrategy{

    @Override
    public void sendMessage(LabDevice device){
        System.out.println("低价值设备，无需通知：" + device.getDeviceName());
    }
}
