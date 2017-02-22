package org.seckill.dto;

/**
 * 暴露秒杀接口
 * Created by yuhm on 2017/2/21.
 */
public class Exposer {

    //是否开启秒杀
    private boolean exposer;

    //一种加密措施
    private String md5;

    private long seckillId;

    //系统当前时间（毫秒）
    private long now;

    //开启时间
    private long start;

    //结束时间
    private long end;

    @Override
    public String toString() {
        return "Exposer{" +
                "exposer=" + exposer +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public Exposer(boolean exposer, String md5, long seckillId) {
        this.exposer = exposer;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposer,long seckillId, long now, long start, long end) {
        this.exposer = exposer;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposer, long seckillId) {
        this.exposer = exposer;
        this.seckillId = seckillId;
    }

    public boolean isExposer() {
        return exposer;
    }

    public void setExposer(boolean exposer) {
        this.exposer = exposer;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
