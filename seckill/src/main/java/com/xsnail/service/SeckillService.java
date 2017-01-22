package com.xsnail.service;

import com.xsnail.dto.Exposer;
import com.xsnail.dto.SeckillExecution;
import com.xsnail.entity.Seckill;
import com.xsnail.exception.RepeatKillException;
import com.xsnail.exception.SeckillCloseException;
import com.xsnail.exception.SeckillException;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */
public interface SeckillService {
    List<Seckill> getSeckillList();
    Seckill getSeckillById(long seckillId);
    Exposer exportSeckillUrl(long seckillId);
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
