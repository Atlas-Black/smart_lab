package com.yiguan.smart_lab.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginVO {
    private Long userId;
    private String token;
}
