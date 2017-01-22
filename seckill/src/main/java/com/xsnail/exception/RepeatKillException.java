package com.xsnail.exception;

/**
 * Created by Administrator on 2016/12/18.
 */

/**
 * 重复秒杀异常(运行时异常)
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
