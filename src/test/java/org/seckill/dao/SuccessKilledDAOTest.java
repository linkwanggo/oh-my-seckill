package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDAOTest {

    @Autowired
    SuccessKilledDAO successKilledDAO;

    @Test
    public void insertSuccessKilled() {
        long seckillId = 1001L;
        long userPhone = 15515622393L;
        int updateCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
        System.out.println(updateCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long seckillId = 1001L;
        long userPhone = 15515622393L;
        SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}