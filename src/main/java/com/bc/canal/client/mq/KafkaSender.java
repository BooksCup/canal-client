package com.bc.canal.client.mq;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;

/**
 * kafka发送类
 * @author zhou
 */
public class KafkaSender implements MqSender {
    private static final Logger logger = Logger.getLogger(KafkaSender.class);

    @Override
    public void send(List<String> dataList) {
        //后面补上异步消息的逻辑
        Properties props = new Properties();
        props.put("bootstrap.servers", CanalClient.kafkaBootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        try {
            try {
                for (String data : dataList) {
                    String messageNo = UUID.randomUUID().toString().replaceAll("-", "");
                    producer.send(new ProducerRecord<>(CanalClient.kafkaTopic,
                            messageNo, data));
                }
            } finally {
                producer.close();
            }
        } catch (Exception e) {
            logger.error("Fail to send data from kafka: " + e.getMessage());
        }
    }
}
