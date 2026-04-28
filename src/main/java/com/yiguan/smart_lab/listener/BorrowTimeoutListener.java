package com.yiguan.smart_lab.listener;

import com.yiguan.smart_lab.config.RabbitMqConfig;
import com.yiguan.smart_lab.dto.BorrowTimeoutMessage;
import com.yiguan.smart_lab.mapper.BorrowRecordMapper;
import com.yiguan.smart_lab.mapper.LabDeviceMapper;
import com.yiguan.smart_lab.model.BorrowRecord;
import com.yiguan.smart_lab.model.LabDevice;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BorrowTimeoutListener{

    private final BorrowRecordMapper borrowRecordMapper;
    private final LabDeviceMapper labDeviceMapper;

    public BorrowTimeoutListener(BorrowRecordMapper borrowRecordMapper,
                                 LabDeviceMapper labDeviceMapper){
        this.borrowRecordMapper = borrowRecordMapper;
        this.labDeviceMapper = labDeviceMapper;
    }

    @RabbitListener(queues = RabbitMqConfig.BORROW_DLX_QUEUE)
    public void handleTimeout(BorrowTimeoutMessage message){
        System.out.println("收到借用超时检查：" + message);

        BorrowRecord record = borrowRecordMapper.selectById(message.getBorrowRecordId());
        if(record == null){
            return;
        }

        //只有借用中/已预约未取用才自动取消
        if(record.getBorrowStatus() != null && record.getBorrowStatus() == 1){
            record.setBorrowStatus(3);
            borrowRecordMapper.updateById(record);

            LabDevice device = labDeviceMapper.selectById(message.getDeviceId());
            if(device!= null){
                device.setStatus(0);
                labDeviceMapper.updateById(device);
            }

            System.out.println("借用超时未取用，系统已自动取消，deviceId = " + message.getDeviceId());
        }
    }
}
