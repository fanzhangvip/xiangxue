package com.enjoy.zero.cache;

/**
 * [享学课堂] {@link https://enjoy.ke.qq.com}
 * 学无止境，让学习成为一种享受
 * TODO: 主讲Zero老师QQ 2124346685
 * TODO: 往期课程咨询芊芊老师QQ 2130753077
 * TODO: VIP课程咨询安生老师QQ 669100976
 * 类说明:
 */
public class CacheElement {

    private Object objectValue;
    private Object objectKey;
    private int index;
    private int hitCount;// getters and setters

    public CacheElement() {
    }

    public CacheElement(Object objectValue, Object objectKey, int index, int hitCount) {
        this.objectValue = objectValue;
        this.objectKey = objectKey;
        this.index = index;
        this.hitCount = hitCount;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    public Object getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(Object objectKey) {
        this.objectKey = objectKey;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}
