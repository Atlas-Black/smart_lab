package com.yiguan.smart_lab.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("borrow_record")
public class BorrowRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long deviceId;
    private Integer borrowStatus;
    private LocalDateTime borrowTime;
    private LocalDateTime expectedReturnTime;
    private LocalDateTime actualReturnTime;
    private Long approveBy;
    private LocalDateTime approveTime;
    private String reason;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
