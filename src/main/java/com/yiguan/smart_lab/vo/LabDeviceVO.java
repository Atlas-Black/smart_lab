package com.yiguan.smart_lab.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabDeviceVO {
    private Long id;
    private String deviceName;
    private String deviceType;
    private String labName;
    private Integer status;
    private String imageUrl;
    private String remark;
    private Integer version;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
