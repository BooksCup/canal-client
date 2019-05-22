package com.bc.canal.client.mq;

import java.util.List;

/**
 * MQ消息发送者
 * @author zhou
 */
public interface MqSender {
    /**
     * 发送数据
     * @param dataList 数据列表
     */
    void send(List<String> dataList);
}
