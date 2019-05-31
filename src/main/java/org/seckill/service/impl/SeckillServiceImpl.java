package org.seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.seckill.dao.SeckillDAO;
import org.seckill.dao.SuccessKilledDAO;
import org.seckill.dao.cache.RedisDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatSeckillException;
import org.seckill.exception.SeckillClosedException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    // md5盐
    private final String salt = "aslfdjaskfhjkfhjeh3jrh23kej2kj";

    @Autowired
    private SeckillDAO seckillDAO;

    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private SuccessKilledDAO successKilledDAO;

    public List<Seckill> getSeckillList() {
        return seckillDAO.queryAll(0, 100);
    }

    public Seckill getById(long seckillId) {
        return seckillDAO.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        // 使用Redis优化暴露接口
        Seckill seckill = redisDAO.getSeckill(seckillId);
        if (seckill == null) {
            seckill = seckillDAO.queryById(seckillId);
            if (seckill == null) {
                // 秒杀不存在
                return new Exposer(false, seckillId);
            } else {
                redisDAO.putSeckill(seckill);
            }
        }
        // 判断时间是否在秒杀时间范围内
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        // 秒杀时间不再范围内  返回当前系统时间
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, now, startTime, endTime);
        }
        // 成功
        String md5 = getMD5(seckillId);
        return new Exposer(true, seckillId, md5);
    }

    private String getMD5(long seckillId) {
        String md5 = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(md5.getBytes());
    }

    /**
     * 使用注解控制事务方法的优点：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格。
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法之外。
     * 3.不是所有方法都需要事务，如只有一条修改操作，只读操作不需要事务控制。
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatSeckillException, SeckillClosedException, SeckillException {
        // 执行秒杀逻辑：减库存 + 添加秒杀明细 -> 优化：先添加秒杀明细再减库存
        // 执行秒杀：检查用户传来的md5
        try {
            if (md5 == null || !md5.equals(getMD5(seckillId))) {
                throw new SeckillException("秒杀数据篡改");
            }
            // 添加秒杀明细
            int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 添加秒杀明细失败
                throw new RepeatSeckillException("重复秒杀");
            } else {
                // 减库存
                Date killTime = new Date();
                int updateCount = seckillDAO.reduceNumber(seckillId, killTime);
                if (updateCount <= 0) {
                    // 没有更新件库存记录 秒杀失败
                    throw new SeckillClosedException("秒杀结束");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatSeckillException re) {
            throw re;
        } catch (SeckillClosedException sce) {
            throw sce;
        } catch (Exception e) {
            // 将所有编译期错误转化为运行期错误
            throw new SeckillException("秒杀系统异常:" + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("秒杀数据篡改");
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("userPhone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try {
            // 存储过程执行 result将被赋值
            seckillDAO.killByProcedure(map);
            // 获取result
            Integer result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                // 秒杀成功
                SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                // 秒杀失败 通过result code获取相应错误信息
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
