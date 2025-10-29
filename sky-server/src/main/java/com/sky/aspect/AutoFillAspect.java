package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    // 切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPoint(){}

    // 前置通知，在方法执行前进行数据填充
    @Before("autoFillPoint()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行数据填充");

        // 获取当前被拦截的方法上的数据库操作
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();  //获得数据库操作

        // 获取当前被拦截的方法参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        Object object = args[0];

        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();


        // 根据对应的数据库操作类型，为对应的属性赋值
        if (value == OperationType.INSERT){
            // 插入
            // setFieldValue(object, "createTime", now);
            // setFieldValue(object, "updateTime", now);
            // setFieldValue(object, "createUser", currentId);
            // setFieldValue(object, "updateUser", currentId);
            // 填充创建时间、更新时间和创建人、更新人
            try {
                Method setCreateTime = object.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = object.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = object.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                // 通过反射设置值
                setCreateTime.invoke(object, now);
                setCreateUser.invoke(object, currentId);
                setUpdateTime.invoke(object, now);
                setUpdateUser.invoke(object, currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (value == OperationType.UPDATE) {
            // 为两个公共字段赋值
            try {
                Method setUpdateTime = object.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setUpdateTime.invoke(object, now);
                setUpdateUser.invoke(object, currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
