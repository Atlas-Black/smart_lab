package com.yiguan.smart_lab.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiguan.smart_lab.annotation.AuditLog;
import com.yiguan.smart_lab.context.UserContext;
import com.yiguan.smart_lab.model.SysLog;
import com.yiguan.smart_lab.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditLogAspect {
    private final SysLogService sysLogService;
    private final ObjectMapper objectMapper;

    public AuditLogAspect(SysLogService sysLogService, ObjectMapper objectMapper){
        this.sysLogService = sysLogService;
        this.objectMapper = objectMapper;
    }

    @AfterReturning("@annotation(auditLog)")
    public void recordLog(JoinPoint joinPoint, AuditLog auditLog){
        try{
            SysLog sysLog = new SysLog();
            sysLog.setUserId(UserContext.getUserId());
            sysLog.setModuleName(auditLog.module());
            sysLog.setOperationType(auditLog.operation());
            sysLog.setMethodName(joinPoint.getSignature().toShortString());
            sysLog.setRequestParams(objectMapper.writeValueAsString(Arrays.toString(joinPoint.getArgs())));

            sysLogService.saveLog(sysLog);

            System.out.println("AOP审计日志记录成功：" + sysLog.getMethodName());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
