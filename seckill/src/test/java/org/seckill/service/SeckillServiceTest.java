package org.seckill.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;

import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;


import static org.junit.Assert.*;

/**
 * Created by yuhm on 2017/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-Context.xml","classpath:spring-service.xml"})
public class SeckillServiceTest {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillLists() throws Exception {
        List<Seckill> list = seckillService.getSeckillLists();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() throws Exception {
        Seckill seckill = seckillService.getById(1000L);
        logger.info("seckill={}",seckill);
    }

    /**
     * 一个测试两个方法
     * exporSeckillUrl
     * executeSeckill
     * 集成测试代码完整逻辑，注意可重复执行
     * @throws Exception
     */
    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1001;
        Exposer exposer = seckillService.exporSeckillUrl(id);
        if(exposer.isExposer()){
            logger.info("exposer={}",exposer);
            long phone = 120;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExcution = seckillService.executeSeckill(id,phone,md5);
                logger.info("seckillExcution={}",seckillExcution);
            } catch (RepeatKillException e){
                logger.error(e.getMessage());
            } catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        } else {
            //秒杀结束
            logger.warn("expose={}",exposer);
        }


        //测试的时候要确保结束时间大于当前时间，才能进行秒杀
        //exposer=Exposer{exposer=true, md5='d2ac0596e5bf3567dfd6d5b361435bb9', seckillId=1000, now=0, start=0, end=0}


  }

    @Test
    public void testExecuteSeckill() throws Exception {
        long id =1000;


        /**
         * 13:23:30.266 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Registering transaction synchronization for SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.274 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@ceda7dd] will be managed by Spring
         13:23:30.281 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>  Preparing: update seckill set number = number - 1 where seckill_id = ? and start_time <= ? and end_time >= ? and number > 0;
         13:23:30.313 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2017-02-22 13:23:30.249(Timestamp), 2017-02-22 13:23:30.249(Timestamp)
         13:23:30.348 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 1
         13:23:30.348 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.348 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd] from current transaction
         13:23:30.349 [main] DEBUG o.s.d.S.insertSuccessKilled - ==>  Preparing: insert ignore into success_killed(seckill_id,user_phone,state) value(?,?,0)
         13:23:30.349 [main] DEBUG o.s.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 110(Long)
         13:23:30.418 [main] DEBUG o.s.d.S.insertSuccessKilled - <==    Updates: 1
         13:23:30.436 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.439 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd] from current transaction
         13:23:30.444 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==>  Preparing: select sk.seckill_id, sk.user_phone, sk.create_time, s.seckill_id "seckill.seckill_id", s.name "seckill.name", s.number "seckill.number", s.start_time "seckill.start_time", s.end_time "seckill.end_time", s.create_time "seckill.create_time" from success_killed sk inner join seckill s on sk.seckill_id = s.seckill_id where sk.user_phone = ? and sk.seckill_id = ?
         13:23:30.445 [main] DEBUG o.s.d.S.queryByIdWithSeckill - ==> Parameters: 110(Long), 1000(Long)
         13:23:30.512 [main] DEBUG o.s.d.S.queryByIdWithSeckill - <==      Total: 1
         13:23:30.517 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.518 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization committing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.519 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization deregistering SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.519 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Transaction synchronization closing SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@639cc8fd]
         13:23:30.570 [main] INFO  o.seckill.service.SeckillServiceTest - seckillExcution=SeckillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1000, userPhone=110, state=0, createTime=Wed Feb 22 13:23:30 CST 2017}}

         */
        //seckillExcution=SeckillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1000, userPhone=110, state=0, createTime=Wed Feb 22 13:23:30 CST 2017}}

    }
}