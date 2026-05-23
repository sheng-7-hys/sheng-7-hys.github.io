package com.sheng.blog.common;

import lombok.Getter;

/**
 * 博客业务异常类
 */
@Getter
public class BlogException extends RuntimeException {

    /** 错误码 */
    private final Integer code;

    /**
     * 构造业务异常（默认状态码 500）
     *
     * @param message 错误信息
     */
    public BlogException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造业务异常（自定义状态码）
     *
     * @param code    状态码
     * @param message 错误信息
     */
    public BlogException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
