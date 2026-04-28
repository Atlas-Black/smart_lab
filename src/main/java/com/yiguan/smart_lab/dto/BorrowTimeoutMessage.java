package com.yiguan.smart_lab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTimeoutMessage implements Serializable {
    private Long borrowRecordId;
    private Long deviceId;
}
