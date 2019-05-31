package org.seckill.dto;

import lombok.Data;

@Data
public class SeckillResult<T> {

    // 本次请求返回是否成功  非秒杀是否开启
    private boolean success;

    // 数据
    private T data;

    // 失败信息
    private String error;

    /**
     * 成功构造
     * @param success
     * @param data
     */
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 失败构造
     * @param success
     * @param error
     */
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
