package com.yiguan.smart_lab.service.impl;

import com.yiguan.smart_lab.mapper.SysLogMapper;
import com.yiguan.smart_lab.model.SysLog;
import com.yiguan.smart_lab.service.SysLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService {

    private final SysLogMapper sysLogMapper;

    public SysLogServiceImpl(SysLogMapper sysLogMapper){
        this.sysLogMapper = sysLogMapper;
    }

    @Override
    public void saveLog(SysLog sysLog){
        sysLogMapper.insert(sysLog);
    }
}
