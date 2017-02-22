package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;

/**
 * Created by yuhm on 2017/2/17.
 */
public interface SuccessKilledDao {

    /**
     * 查看一个手机号码秒杀的所有商品
     * @param usephone
     * @return
     */
    Seckill selectByPhoneNumber(@Param("usephone") long usephone, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查看一个手机号码秒杀的数量
     * @param userphone
     * @return
     */
    int successKilledCount(long userphone);

    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的结果集数量0
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilles并携带秒杀产品
     * @param usePhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill (@Param("seckillId")long seckilId, @Param("usePhone")long usePhone);



}
