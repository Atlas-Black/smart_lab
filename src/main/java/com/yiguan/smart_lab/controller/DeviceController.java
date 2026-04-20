package com.yiguan.smart_lab.controller;

import com.yiguan.smart_lab.annotation.AuditLog;
import com.yiguan.smart_lab.common.Result;
import com.yiguan.smart_lab.service.LabDeviceService;
import com.yiguan.smart_lab.vo.LabDeviceVO;
import com.alibaba.excel.EasyExcel;
import com.yiguan.smart_lab.dto.LabDeviceImportDTO;
import com.yiguan.smart_lab.listener.LabDeviceImportListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final LabDeviceService labDeviceService;

    public DeviceController(LabDeviceService labDeviceService){
        this.labDeviceService = labDeviceService;
    }

    @GetMapping("/{id}")
    public Result<LabDeviceVO> getDeviceById(@PathVariable Long id){
        return Result.success(labDeviceService.getDeviceById(id));
    }

    @PostMapping("/import")
    public Result<Void> importDevice(@RequestParam("file") MultipartFile file) throws Exception{
        EasyExcel.read(file.getInputStream(), LabDeviceImportDTO.class, new LabDeviceImportListener(labDeviceService))
                .sheet()
                .doRead();

        return Result.success(null);
    }

    @AuditLog(module = "设备管理", operation = "借用设备")
    @PostMapping("/borrow/{deviceId}")
    public Result<Void> borrowDevice(@PathVariable Long deviceId){
        labDeviceService.borrowDevice(deviceId);
        return Result.success(null);
    }
}
