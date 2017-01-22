package com.xsnail.service.impl;

import com.xsnail.dao.RedisDao;
import com.xsnail.dao.SeckillDao;
import com.xsnail.dao.SuccessKilledDao;
import com.xsnail.dto.Exposer;
import com.xsnail.dto.SeckillExecution;
import com.xsnail.entity.Seckill;
import com.xsnail.entity.SuccessKilled;
import com.xsnail.enums.SeckillStatEnum;
import com.xsnail.exception.RepeatKillException;
import com.xsnail.exception.SeckillCloseException;
import com.xsnail.exception.SeckillException;
import com.xsnail.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */
@Service
public class SeckillServiceImpl implements SeckillService{
    private Logger  logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    //md5盐值字符串,用于混淆md5
    private final String slat = "skdl,432ilsdldsdi211l993'd;dke";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null) {
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException{
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date date = new Date();
        try {
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
//            int updateCount = seckillDao.reduceNumber(seckillId, date);
            if (insertCount <= 0) {
                throw new SeckillCloseException("seckill repeated");
            } else {
//                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //mysql update 会加入行级锁
                int updateCount = seckillDao.reduceNumber(seckillId, date);
                if (updateCount <= 0) {
                    //rollback
                    throw new RepeatKillException("seckill is closed");
                } else {
                    //commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2) {
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }

    private String getMd5(long seckillId){
        String base = seckillId + "/" +slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
