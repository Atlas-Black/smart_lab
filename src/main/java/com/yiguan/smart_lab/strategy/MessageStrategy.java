package com.yiguan.smart_lab.strategy;

import com.yiguan.smart_lab.model.LabDevice;

public interface MessageStrategy {
    void sendMessage(LabDevice device);
}
