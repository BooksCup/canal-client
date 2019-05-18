package com.bc.canal.client.handler;

import com.bc.canal.client.cons.Constants;
import com.bc.canal.client.mq.KafkaSender;
import com.bc.canal.client.mq.MqSender;
import com.bc.canal.client.mq.RabbitmqSender;
import com.bc.canal.client.mq.RedisSender;

import java.util.concurrent.ConcurrentHashMap;

/**
 * MQ处理者工厂
 * @author zhou
 */
public class MqSenderHandler {

    static ConcurrentHashMap<String, MqSender> handlerMap = new ConcurrentHashMap<>();

    static {
        handlerMap.put(Constants.CANAL_MQ_RABBITMQ, new RabbitmqSender());
        handlerMap.put(Constants.CANAL_MQ_REDIS, new RedisSender());
        handlerMap.put(Constants.CANAL_MQ_KAFKA, new KafkaSender());
    }

    public static MqSender getMqSender(String mqType) {
        return handlerMap.get(mqType) == null ?
                handlerMap.get(Constants.CANAL_MQ_RABBITMQ) : handlerMap.get(mqType);
    }
}
