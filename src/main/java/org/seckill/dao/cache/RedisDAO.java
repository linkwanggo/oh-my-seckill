package org.seckill.dao.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.seckill.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
public class RedisDAO {

    private final JedisPool jedisPool;

    public RedisDAO(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    // 使用Jackson作为序列化工具
    private ObjectMapper mapper = new ObjectMapper();

    public Seckill getSeckill(long seckillId) {
        log.debug("------------GET Seckill from Redis-----------");
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            // Object序列化到redis -> 反序列化
            String jsonString = jedis.get(key);
            if (jsonString != null) {
                Seckill seckill = mapper.readValue(jsonString, Seckill.class);
                return seckill;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        log.debug("------------PUT Seckill from Redis-----------");
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getSeckillId();
            // Object序列化到redis
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonString = mapper.writeValueAsString(seckill);
            int timeout = 60 * 60;
            String res = jedis.setex(key,timeout, jsonString);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
