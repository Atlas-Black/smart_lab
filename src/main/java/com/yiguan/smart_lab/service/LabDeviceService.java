package com.yiguan.smart_lab.service;


import com.yiguan.smart_lab.vo.LabDeviceVO;

public interface LabDeviceService {
    LabDeviceVO getDeviceById(Long id);

    void saveImportedDevice(com.yiguan.smart_lab.model.LabDevice labDevice);

    void borrowDevice(Long deviceId);
}
