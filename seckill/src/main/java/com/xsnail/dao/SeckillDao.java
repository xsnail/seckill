package com.xsnail.dao;

import com.xsnail.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime 执行秒杀的时间
     * @return 影响的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    Seckill queryById(long seckillId);

    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
