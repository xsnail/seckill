package com.xsnail.dto;

import com.xsnail.entity.SuccessKilled;
import com.xsnail.enums.SeckillStatEnum;

/**
 * Created by Administrator on 2016/12/18.
 */
public class SeckillExecution {
    private long seckilled;
    private int state;
    private String stateInfo;
    private SuccessKilled successKilled;

    public SeckillExecution(long seckilled, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckilled = seckilled;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckilled,SeckillStatEnum statEnum) {
        this.seckilled = seckilled;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckilled() {
        return seckilled;
    }

    public void setSeckilled(long seckilled) {
        this.seckilled = seckilled;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
