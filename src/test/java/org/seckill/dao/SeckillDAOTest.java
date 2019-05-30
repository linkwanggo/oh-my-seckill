package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDAOTest {

    @Autowired
    SeckillDAO seckillDAO;

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int updateCount = seckillDAO.reduceNumber(1000, killTime);
        System.out.println(updateCount);
    }

    @Test
    public void queryById() {
        long seckillId = 1000;
        Seckill seckill = seckillDAO.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckillList = seckillDAO.queryAll(0, 100);
        for (Seckill seckill: seckillList) {
            System.out.println(seckill);
        }
    }
}