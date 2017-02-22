package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by yuhm on 2017/2/21.
 */
//@Service @Dao @Controller  @Component
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = (Logger) LoggerFactory.getLogger(SeckillServiceImpl.class);

    //注入Service依赖
    //@Autowired @Resource @Inject
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    //md5颜值字符串，用户混淆视MD5
    public final String slat = "adshldjhlaksjkl;1541#324u290p-0-&((&*%&*";

    public List<Seckill> getSeckillLists() {
        return seckillDao.queryAll(0, 100);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    //展示秒杀的接口地址
    public Exposer  exporSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime  = seckill.getEndTime();
        //系统时间
        Date nowTime = new Date();
        if(nowTime.getTime() <startTime.getTime()
                || nowTime.getTime() >endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        //md5,转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    //md5的实现方法，抽象出来，因为待会还是要用
    private String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        //spring 的生成 MD5的方法
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解控制方法的优点
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能要短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外
     * 3：不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null ||  !md5.equals(getMD5((seckillId)))){
            throw new SeckillCloseException("seckill date rewrite");
        }
        //秒杀逻辑：减库存 + 记录购买明细
        Date nowTime = new Date();
        try{
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if(updateCount <= 0 ){
                //没有更新到记录，秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //唯一： seckillId ,userPhone
                if(insertCount <= 0){
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId , SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            //所有编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }
}
