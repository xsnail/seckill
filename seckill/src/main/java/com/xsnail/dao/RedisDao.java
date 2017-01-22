package com.xsnail.dao;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.xsnail.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Administrator on 2016/12/22.
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    private final JedisPool jedisPool;

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            //并没有实现内部序列化操作
            //get-->byte[]-->反序列化-->Object(Seckill)
            //采用自定义序列化方式
            //protostuff : pojo
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                Seckill seckill = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        // eckill-->序列化-->byte[]
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //缓存时间
            int timeout = 60 * 60; //1小时
            String result = jedis.setex(key.getBytes(),timeout,bytes);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }
}
