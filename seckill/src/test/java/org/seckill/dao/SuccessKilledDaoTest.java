package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import static org.junit.Assert.*;

/**
 * Created by yuhm on 2017/2/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-Context.xml")
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testSelectByPhone() throws Exception {
        long phone = 18096772628L;
        int offset = 0;
        int limit = 10;
        Seckill seckills = successKilledDao.selectByPhoneNumber(phone,offset,limit);
        System.out.println("seckills = "+seckills.getName()+",number = "+seckills.getNumber());
    }

    @Test
    public void successKilledCount() throws Exception {
        long phone = 18096772628L;
        int phoneCount = successKilledDao.successKilledCount(phone);
        System.out.println("phoneCount=" + phoneCount);

    }

    @Test
    public void testInsertSuccessKilled() throws Exception {
        long seckillId = 1003L;
        long userPhone = 18096898989L;

        int successKilledCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("successKilledCount="+successKilledCount);



    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
//        long seckillId = 1001L;
//        long userPhone = 18096772628L;
        long seckillId = 1003L;
        long userPhone = 18096898989L;

        SuccessKilled successKilleds  = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);

        System.out.println("phone = "+successKilleds.getUserPhone()+"  ,  seckillName = "+ successKilleds.getSeckill().getName());
        System.out.println("seckill : "+successKilleds.getSeckill());

    }


}