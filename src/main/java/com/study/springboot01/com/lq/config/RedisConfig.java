package com.study.springboot01.com.lq.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-04 21:12
 * Editored:
 */
@Configurable
public class RedisConfig {
    @Value("${spring.redis.type}")
    private Integer type;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.pool.max-active}")
    private Integer MaxActive;
    @Value("${spring.redis.pool.min-idle}")
    private Integer MinIdle;
    @Value("${spring.redis.pool.max-wait}")
    private Integer MaxWait;
    @Value("${spring.redis.pool.max-idle}")
    private Integer MaxIdle;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.sentinel.nodes}")
    private String nodes;
    @Value("${spring.redis.sentinel.master}")
    private String masterName;

    //1注入JedisPoolConfig
    @Bean
    public JedisPoolConfig getJedisConfig(){
        JedisPoolConfig jedconfig = new JedisPoolConfig();
        jedconfig.setMaxIdle(MaxIdle);
        jedconfig.setMaxWaitMillis(MaxWait);
        jedconfig.setMinIdle(MinIdle);
        jedconfig.setMaxTotal(MaxActive);
        return jedconfig;
    }

    //2.注入redis哨兵配置config
    @Bean
    public RedisSentinelConfiguration sentinelConfiguration(){
        if(type==1){
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            //配置哨兵的master的名称
            redisSentinelConfiguration.setMaster(masterName);
            //配置redis的哨兵sentinel
            String[] RedisNodes = nodes.split(",");
            Set<RedisNode> redisNodeSet=new HashSet<>();
            for (String node :RedisNodes) {
                String[] hostAndport = node.split(":");
                redisNodeSet.add(new RedisNode(hostAndport[0],Integer.parseInt(hostAndport[1])));
            }
            redisSentinelConfiguration.setSentinels(redisNodeSet);
            return redisSentinelConfiguration;
        }else{
            return new RedisSentinelConfiguration();
        }
    }

    //3:配置jedis工厂
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig, RedisSentinelConfiguration sentinelConfig) {
        if(type==1){
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig);
        jedisConnectionFactory.setHostName(masterName);
            jedisConnectionFactory.setPassword(password);
            return jedisConnectionFactory;
        }else{
            JedisPoolConfig config = getJedisConfig();
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
            jedisConnectionFactory.setHostName(host);
            jedisConnectionFactory.setPassword(password);
            jedisConnectionFactory.setPort(port);
            jedisConnectionFactory.setTimeout(timeout);
            return jedisConnectionFactory;
        }
    }
    //4:初始化redisTemplate
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        if(type==1){
            //使用fastjson序列化
            FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
            // value值的序列化采用fastJsonRedisSerializer
            template.setValueSerializer(fastJsonRedisSerializer);
            template.setHashValueSerializer(fastJsonRedisSerializer);
            // key的序列化采用StringRedisSerializer
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setConnectionFactory(redisConnectionFactory);
            // 开启事务
            template.setEnableTransactionSupport(true);
            return template;
        }else{
            template.setConnectionFactory(new JedisConnectionFactory(new RedisSentinelConfiguration(),getJedisConfig()));
            return template;
        }
    }
}
