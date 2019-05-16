package com.bc.canal.client.mq;

import java.util.List;

import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;

import redis.clients.jedis.Jedis;

/**
 * @author zhou
 */
public class RedisSender {
    private static final Logger logger = Logger.getLogger(RedisSender.class);

    public static void send(List<String> dataList) {
        try {
            Jedis jedis = null;
            try {
                jedis = new Jedis(CanalClient.redisHost,
                        CanalClient.redisPortInt);
                for (String data : dataList) {
                    jedis.rpush(CanalClient.redisQueuename, data);
                }
            } catch (Exception e) {
                logger.error("redis send msg error: " + e.getMessage());
            } finally {
                if (null != jedis) {
                    jedis.close();
                }
            }
        } catch (Exception e) {
            logger.error("fail to send data from redis, error: " + e.getMessage());
        }
    }
}
