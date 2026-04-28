package com.yiguan.smart_lab.strategy;

import com.yiguan.smart_lab.model.LabDevice;
import org.springframework.stereotype.Component;

@Component("MEDIUM")
public class StationLetterStrategy implements MessageStrategy{

    @Override
    public void sendMessage(LabDevice device){
        System.out.println("发送站内信通知，设备：" + device.getDeviceName());
    }
}
