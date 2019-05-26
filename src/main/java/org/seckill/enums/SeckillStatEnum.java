package org.seckill.enums;

/**
 *  秒杀状态枚举
 */
public enum  SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改")
    ;

    // 状态码
    private int state;

    // 信息
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 通过状态码获取SeckillStatEnum
     * @param index
     * @return
     */
    public static SeckillStatEnum stateOf(int index) {
        for (SeckillStatEnum seckillStatEnum: values()) {
            if (seckillStatEnum.getState() == index) {
                return seckillStatEnum;
            }
        }
        return null;
    }
}
