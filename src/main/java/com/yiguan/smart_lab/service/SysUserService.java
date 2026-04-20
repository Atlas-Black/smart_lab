package com.yiguan.smart_lab.service;

import com.yiguan.smart_lab.dto.LoginDTO;
import com.yiguan.smart_lab.vo.LoginVO;

public interface SysUserService {
    LoginVO login(LoginDTO loginDTO);
    void saveImportedDevice(com.yiguan.smart_lab.model.LabDevice labDevice);
}
