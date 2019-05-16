package com.bc.canal.client.mq;

import java.util.List;

import com.bc.canal.client.cons.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * Rabbitmq发送类
 *
 * @author zhou
 */
public class RabbitmqSender {
    private static final Logger logger = Logger.getLogger(RabbitmqSender.class);

    public static void send(List<String> dataList) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CanalClient.rabbitmqHost);
        factory.setPort(CanalClient.rabbitmqPortInt);
        factory.setUsername(CanalClient.rabbitmqUser);
        factory.setPassword(CanalClient.rabbitmqPass);
        // routingKey
        String routingKey = CanalClient.rabbitmqRoutingKey;

        try {
            boolean durable = false;
            if (Constants.RABBITMQ_DURABLE_TRUE.equalsIgnoreCase(CanalClient.rabbitmqDurable)) {
                durable = true;
            }
            Connection connection = null;
            Channel channel = null;
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();
                if (StringUtils.isEmpty(routingKey)) {
                    routingKey = CanalClient.rabbitmqQueuename;
                }

                // 声明交换机类型
                if (Constants.RABBITMQ_EXCHANGE_TYPE_DIRECT.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    // direct
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, CanalClient.rabbitmqExchangeType);
                    channel.queueDeclare(CanalClient.rabbitmqQueuename, durable, false, false, null);
                    channel.queueBind(CanalClient.rabbitmqQueuename, CanalClient.rabbitmqExchangeName, routingKey);

                } else if (Constants.RABBITMQ_EXCHAGE_TYPE_TOPIC.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    //topic
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, CanalClient.rabbitmqExchangeType);
                    channel.queueDeclare(CanalClient.rabbitmqQueuename, durable, false, false, null);
                    channel.queueBind(CanalClient.rabbitmqQueuename, CanalClient.rabbitmqExchangeName, routingKey);

                } else if (Constants.RABBITMQ_EXCHANGE_TYPE_FANOUT.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    // fanout
                    // fanout广播行为,无需配置routingKey
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, CanalClient.rabbitmqExchangeType);

                } else {
                    // unknown
                    logger.error("unknown exchange type: " + CanalClient.rabbitmqExchangeType);
                    return;

                }
                for (String data : dataList) {
                    channel.basicPublish(CanalClient.rabbitmqExchangeName, routingKey,
                            MessageProperties.PERSISTENT_TEXT_PLAIN, data.getBytes());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (null != channel) {
                    channel.close();
                }
                if (null != connection) {
                    connection.close();
                }
            }
        } catch (Exception e) {
            logger.error("fail to send data to rabbitmq, error: " + e.getMessage());
        }

    }
}
