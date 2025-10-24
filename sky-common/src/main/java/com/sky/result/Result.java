package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
// 使用泛型 T 的核心目的是：让 data 字段可以承载任意类型的返回数据，同时保持类型安全
// Serializable 是一个标记接口（Marker Interface），表示这个类的对象可以被 序列化（Serialization） —— 也就是能转换成字节流
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    // static 方法是属于类本身的，不依赖于任何泛型参数
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
