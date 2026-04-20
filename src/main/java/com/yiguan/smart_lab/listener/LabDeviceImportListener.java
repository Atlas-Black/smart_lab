package com.yiguan.smart_lab.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.yiguan.smart_lab.dto.LabDeviceImportDTO;
import com.yiguan.smart_lab.model.LabDevice;
import com.yiguan.smart_lab.service.LabDeviceService;

public class LabDeviceImportListener implements ReadListener<LabDeviceImportDTO> {

    private final LabDeviceService labDeviceService;

    public LabDeviceImportListener(LabDeviceService labDeviceService){
        this.labDeviceService = labDeviceService;
    }

    @Override
    public void invoke(LabDeviceImportDTO data, AnalysisContext context){
        LabDevice device= new LabDevice();
        device.setDeviceName(data.getDeviceName());
        device.setDeviceType(data.getDeviceType());
        device.setLabName(data.getLabName());
        device.setStatus(data.getStatus());
        device.setRemark(data.getRemark());

        labDeviceService.saveImportedDevice(device);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context){
        System.out.println("实验室设备 Excel 导入完成");
    }
}
