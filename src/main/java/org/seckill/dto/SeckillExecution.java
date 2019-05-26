package org.seckill.dto;

import lombok.Data;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/**
 * 秒杀结果DTO
 */
@Data
public class SeckillExecution {

    // 秒杀商品id
    private long seckillId;

    // 秒杀状态
    private int state;

    // 秒杀结果信息
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    // 成功情况构造方法
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    // 失败情况构造方法
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getStateInfo();
    }
}

