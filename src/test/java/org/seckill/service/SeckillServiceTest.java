package org.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.exception.RepeatSeckillException;
import org.seckill.exception.SeckillClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
@Slf4j
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        log.info("seckillList={}", seckillList);
    }

    @Test
    public void getById() {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        log.info("seckill={}", seckill);
    }

    @Test
    public void testSeckillLogic() {
        try {
            long seckillId = 1000L;
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            log.info("exposer={}", exposer);
            if (exposer.isExposed()) {
                // 秒杀活动已经开启  执行秒杀操作
                long userPhone = 13838521628L;
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
                log.info("seckillExecution={}", seckillExecution);
                log.info("SuccessKilled={}", seckillExecution.getSuccessKilled());
            } else {
                // 秒杀活动未开启
                log.warn("exposer={}", exposer);
            }
        } catch (RepeatSeckillException re) {
            log.error("重复秒杀异常");
        } catch (SeckillClosedException sce) {
            log.error("秒杀关闭异常");
        }
    }

    @Test
    public void killByProcedure() {
        long seckillId = 1001;
        long userPhone = 11111111111L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, exposer.getMd5());
            log.info("seckillExecution={}", seckillExecution);
            log.info("SuccessKilled={}", seckillExecution.getSuccessKilled());
        } else {
            // 秒杀活动未开启
            log.warn("exposer={}", exposer);
        }
    }
}