package com.yiguan.smart_lab.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lab_device")
public class LabDevice {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String deviceName;
    private String deviceType;
    private String labName;
    private Integer status;
    private String imageUrl;
    private String remark;

    @Version
    private Integer version;

    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String valueLevel;
}
