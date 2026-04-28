package com.yiguan.smart_lab.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiguan.smart_lab.config.RabbitMqConfig;
import com.yiguan.smart_lab.exception.BusinessException;
import com.yiguan.smart_lab.mapper.BorrowRecordMapper;
import com.yiguan.smart_lab.mapper.LabDeviceMapper;
import com.yiguan.smart_lab.model.BorrowRecord;
import com.yiguan.smart_lab.model.LabDevice;
import com.yiguan.smart_lab.service.LabDeviceService;
import com.yiguan.smart_lab.vo.LabDeviceVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LabDeviceServiceImpl implements LabDeviceService {

    private final LabDeviceMapper labDeviceMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final BorrowRecordMapper borrowRecordMapper;
    private final org.redisson.api.RedissonClient redissonClient;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    public LabDeviceServiceImpl (LabDeviceMapper labDeviceMapper,
                                 RedisTemplate<String, Object> redisTemplate,
                                 ObjectMapper objectMapper,
                                 BorrowRecordMapper borrowRecordMapper,
                                 org.redisson.api.RedissonClient redissonClient,
                                 org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate){
        this.labDeviceMapper = labDeviceMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.borrowRecordMapper = borrowRecordMapper;
        this.redissonClient = redissonClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public LabDeviceVO getDeviceById(Long id){
        String key = "lab_device:" + id;

        Object cacheObject = redisTemplate.opsForValue().get(key);
        if(cacheObject != null){
            System.out.println("命中缓存，deviceId = " + id);
            return objectMapper.convertValue(cacheObject, LabDeviceVO.class);
        }

        System.out.println("查询数据库，deviceId = " + id);
        LabDevice device = labDeviceMapper.selectById(id);
        if(device == null){
            throw new BusinessException("设备不存在");
        }

        LabDeviceVO vo = toVO(device);
        redisTemplate.opsForValue().set(key, vo);
        return vo;
    }

    private LabDeviceVO toVO(LabDevice device){
        LabDeviceVO vo = new LabDeviceVO();
        vo.setId(device.getId());
        vo.setDeviceName(device.getDeviceName());
        vo.setDeviceType(device.getDeviceType());
        vo.setLabName(device.getLabName());
        vo.setStatus(device.getStatus());
        vo.setImageUrl(device.getImageUrl());
        vo.setRemark(device.getRemark());
        vo.setVersion(device.getVersion());
        vo.setCreateBy(device.getCreateBy());
        vo.setCreateTime(device.getCreateTime());
        vo.setUpdateTime(device.getUpdateTime());
        return vo;
    }

    @Override
    public void saveImportedDevice(LabDevice labDevice){
        labDeviceMapper.insert(labDevice);
    }

    @Override
    public void borrowDevice(Long deviceId){
        String lockKey = "lock:lab_device:" + deviceId;
        org.redisson.api.RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;
        try{
            locked = lock.tryLock();

            if(!locked){
                throw new BusinessException("当前设备借用人数过多，请稍后再试");
            }

            LabDevice device = labDeviceMapper.selectById(deviceId);
            if(device == null){
                throw new BusinessException("设备不存在");
            }

            if(device.getStatus() == null || device.getStatus() != 0){
                throw new BusinessException("该设备当前不可借用");
            }

            //1.修改设备状态
            device.setStatus(2);
            int updateRows = labDeviceMapper.updateById(device);
            if(updateRows == 0){
                throw new BusinessException("设备状态更新失败，请重试");
            }

            //2.写借用流水
            BorrowRecord record = new BorrowRecord();
            record.setUserId(com.yiguan.smart_lab.context.UserContext.getUserId());
            record.setDeviceId(deviceId);
            record.setBorrowStatus(1);
            record.setBorrowTime(java.time.LocalDateTime.now());
            record.setRemark("用户借用设备");
            borrowRecordMapper.insert(record);
            com.yiguan.smart_lab.dto.BorrowTimeoutMessage message =
                    new com.yiguan.smart_lab.dto.BorrowTimeoutMessage(record.getId(), deviceId);

            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.BORROW_EXCHANGE,
                    RabbitMqConfig.BORROW_ROUTING_KEY,
                    message,
                    msg ->{
                        msg.getMessageProperties().setExpiration("30000");
                        return msg;
                    }
            );

            //3.删除缓存，保证一致性
            String cacheKey = "lab_device:" + deviceId;
            redisTemplate.delete(cacheKey);

        } finally {
            if(locked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
