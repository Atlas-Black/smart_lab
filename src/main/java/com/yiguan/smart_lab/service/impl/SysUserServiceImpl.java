package com.yiguan.smart_lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiguan.smart_lab.dto.LoginDTO;
import com.yiguan.smart_lab.exception.BusinessException;
import com.yiguan.smart_lab.mapper.LabDeviceMapper;
import com.yiguan.smart_lab.mapper.SysUserMapper;
import com.yiguan.smart_lab.model.LabDevice;
import com.yiguan.smart_lab.model.SysUser;
import com.yiguan.smart_lab.service.SysUserService;
import com.yiguan.smart_lab.utils.JwtUtils;
import com.yiguan.smart_lab.vo.LoginVO;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final LabDeviceMapper labDeviceMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper, LabDeviceMapper labDeviceMapper){
        this.sysUserMapper = sysUserMapper;
        this.labDeviceMapper = labDeviceMapper;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO){
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, loginDTO.getUsername())
                .eq(SysUser::getPassword, loginDTO.getPassword());

        SysUser user = sysUserMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == null || user.getStatus() != 1){
            throw new BusinessException("用户已被禁用");
        }

        String token = JwtUtils.createToken(user.getId());
        return new LoginVO(user.getId(), token);
    }

    @Override
    public void saveImportedDevice(LabDevice labDevice){
        labDeviceMapper.insert(labDevice);
    }
}
