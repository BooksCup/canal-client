package com.bc.canal.client.mq;

import java.util.List;

import com.bc.canal.client.cons.Constants;
import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import scala.collection.immutable.Stream;

/**
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

        try {
            boolean durable = false;
            if ("y".equalsIgnoreCase(CanalClient.rabbitmqDurable)) {
                durable = true;
            }
            Connection connection = null;
            Channel channel = null;
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();

                // 声明交换机类型
                if (Constants.RABBITMQ_EXCHANGE_TYPE_DIRECT.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, "direct");
                    channel.queueDeclare(CanalClient.rabbitmqQueuename, durable, false, false, null);
                    for (String data : dataList) {
                        channel.basicPublish("", CanalClient.rabbitmqQueuename,
                                MessageProperties.PERSISTENT_TEXT_PLAIN, data.getBytes());
                    }
                } else if (Constants.RABBITMQ_EXCHAGE_TYPE_TOPIC.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, "topic");
                } else if (Constants.RABBITMQ_EXCHANGE_TYPE_FANOUT.equalsIgnoreCase(CanalClient.rabbitmqExchangeType)) {
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, "fanout");
                    for (String data : dataList) {
                        channel.basicPublish(CanalClient.rabbitmqExchangeName, "",
                                MessageProperties.PERSISTENT_TEXT_PLAIN, data.getBytes());
                    }
                } else {
                    // 默认直连
                    channel.exchangeDeclare(CanalClient.rabbitmqExchangeName, "direct");
                    channel.queueDeclare(CanalClient.rabbitmqQueuename, durable, false, false, null);
                    for (String data : dataList) {
                        channel.basicPublish("", CanalClient.rabbitmqQueuename,
                                MessageProperties.PERSISTENT_TEXT_PLAIN, data.getBytes());
                    }
                }
            } finally {
                channel.close();
                connection.close();
            }
        } catch (Exception e) {
            logger.error("Fail to send data to rabbitmq: " + e.getMessage());
        }

    }
}
