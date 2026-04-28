package com.sheng.blog.common;

import lombok.Data;

/**
 * 统一响应结果封装类
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> {

    /** 状态码：200-成功，其他-失败 */
    private Integer code;

    /** 提示消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     *
     * @param message 错误信息
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（带状态码）
     *
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
