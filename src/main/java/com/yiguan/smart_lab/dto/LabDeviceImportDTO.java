package com.yiguan.smart_lab.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class LabDeviceImportDTO {

    @ExcelProperty("设备名称")
    private String deviceName;

    @ExcelProperty("设备类型")
    private String deviceType;

    @ExcelProperty("所属实验室")
    private String labName;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("备注")
    private String remark;
}
