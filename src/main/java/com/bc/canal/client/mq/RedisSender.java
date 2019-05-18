package com.bc.canal.client.mq;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;

import redis.clients.jedis.Jedis;

/**
 * redis发送类
 * @author zhou
 */
public class RedisSender implements MqSender {
    private static final Logger logger = Logger.getLogger(RedisSender.class);

    @Override
    public void send(List<String> dataList) {
        try {
            Jedis jedis = null;
            try {
                jedis = new Jedis(CanalClient.redisHost,
                        CanalClient.redisPortInt);
                // 配置了redis密码
                if (!StringUtils.isEmpty(CanalClient.redisPassword)) {
                    jedis.auth(CanalClient.redisPassword);
                }
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
