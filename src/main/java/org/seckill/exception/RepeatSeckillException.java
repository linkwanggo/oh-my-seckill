package org.seckill.exception;

import org.seckill.dto.SeckillExecution;

/**
 * 重复秒杀Exception
 */
public class RepeatSeckillException extends SeckillException {

    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }

}
