package com.study.springboot01.com.lq.util.RedisUtil;

import com.study.springboot01.com.lq.util.Stringutil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-04 22:22
 * Editored:
 */
@Slf4j
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    //写入缓存(string-Object型)
    public  boolean set(final String key,Object value){
        boolean flage=false;
        try {
            ValueOperations<Serializable, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value);
            flage = true;
        }catch (Exception e){
            log.error("写入缓存失败："+e);
        }
        return flage;
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public  void setByexpire(final String key, Object value, Long expireTime ,TimeUnit timeUnit) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value,expireTime,timeUnit);
        redisTemplate.expire(key, expireTime, timeUnit);
    }


    /**
     * 重设缓存设置时效时间
     * @param key
     * @return
     */
    public  void expire(final String key, Long expireTime , TimeUnit timeUnit) {
        //String s = get(key);
        //setandtime(key,s,expireTime,timeUnit);
        redisTemplate.expire(key, expireTime, timeUnit);
    }

    /**
     * 批量删除对应的value
     * @param keys
     */
    public  void removeKey(final String[] keys) {
        for (String key : keys) {
            if(exists(key)) {
                redisTemplate.delete(key);
            }
        }
    }
    /**
     * 删除对应的value
     * @param key
     */
    public  void remove(final String key) {
        if(exists(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public  boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存(String-String)
     * @param key
     * @return
     */
    public  String get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return StringUtil.getObjStr(operations.get(key));
    }

    /**
     * 读取缓存(String-Object)
     * @param key
     * @return
     */
    public  Object getByObject(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public  void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public  Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object>  hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 列表添加
     * @param k
     * @param v
     */
    public  void lPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }

    /**
     * 列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }

    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void add(String key,Object value){
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String, Object> set =redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,scoure);
    }

    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }
}
