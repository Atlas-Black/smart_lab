package com.yiguan.smart_lab.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log")
public class SysLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String moduleName;
    private String operationType;
    private String methodName;
    private String requestParams;
    private LocalDateTime operationTime;
}
