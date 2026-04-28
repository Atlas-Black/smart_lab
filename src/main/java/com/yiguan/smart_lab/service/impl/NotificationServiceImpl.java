package com.yiguan.smart_lab.service.impl;

import com.yiguan.smart_lab.exception.BusinessException;
import com.yiguan.smart_lab.mapper.LabDeviceMapper;
import com.yiguan.smart_lab.model.LabDevice;
import com.yiguan.smart_lab.service.NotificationService;
import com.yiguan.smart_lab.strategy.MessageStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final LabDeviceMapper labDeviceMapper;
    private final Map<String, MessageStrategy> strategyMap;

    public NotificationServiceImpl(LabDeviceMapper labDeviceMapper,
                                   Map<String, MessageStrategy> strategyMap){
        this.labDeviceMapper = labDeviceMapper;
        this.strategyMap = strategyMap;
    }

    @Override
    public void notifyByDeviceValue(Long deviceId){
        LabDevice device = labDeviceMapper.selectById(deviceId);
        if(device == null){
            throw new BusinessException("设备不存在");
        }

        String valueLevel = device.getValueLevel();
        MessageStrategy strategy = strategyMap.get(valueLevel);

        if(strategy == null){
            throw new BusinessException("未找到对应的通知策略");
        }

        strategy.sendMessage(device);
    }
}
