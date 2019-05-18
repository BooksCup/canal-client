package com.bc.canal.client.mq;

import java.util.List;

/**
 * MQ消息发送者
 * @author zhou
 */
public interface MqSender {
    void send(List<String> dataList);
}
