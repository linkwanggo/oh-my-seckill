package org.seckill.dto;

import lombok.Data;

import java.util.Date;

/**
 * 秒杀接口DTO
 */
@Data
public class Exposer {

    /**
     * 秒杀商品不存在构造
     * @param exposed
     * @param seckillId
     */
    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }


    /**
     * 在秒杀时间范围内构造
     * @param exposed
     * @param md5
     * @param seckillId
     */
    public Exposer(boolean exposed, long seckillId, String md5) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.md5 = md5;
    }

    /**
     * 不在秒杀时间范围内构造
     * @param exposed
     * @param seckillId
     * @param now
     * @param start
     * @param end
     */
    public Exposer(boolean exposed, long seckillId, Date now, Date start, Date end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    // 秒杀是否开启
    private boolean exposed;

    // 一种加密措施
    private String md5;

    // 秒杀商品id
    private long seckillId;

    // 系统当前时间
    private Date now;

    // 秒杀开始时间
    private Date start;

    // 秒杀结束时间
    private Date end;
}
