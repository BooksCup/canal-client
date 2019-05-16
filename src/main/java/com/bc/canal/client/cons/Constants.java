package com.bc.canal.client.cons;

import java.io.File;

/**
 * @author zhou
 */
public class Constants {
    //CANAL SERVER
    /**
     * CANAL服务端HOST
     */
    public static final String CANAL_SERVER_HOST_KEY = "canal.server.host";

    /**
     * CANAL服务端PORT
     */
    public static final String CANAL_SERVER_PORT_KEY = "canal.server.port";

    /**
     * CANAL服务端实例
     */
    public static final String CANAL_SERVER_INSTANCE_KEY = "canal.server.instance";

    /**
     * CANAL批处理数量
     */
    public static final String CANAL_BATCHSIZE_KEY = "canal.batchsize";

    /**
     * CANAL SLEEP
     */
    public static final String CANAL_SLEEP_KEY = "canal.sleep";

    /**
     * CANAL PRINT
     */
    public static final String CANAL_PRINT_KEY = "canal.print";

    /**
     * CANAL BINLOG FILENAME
     */
    public static final String CANAL_BINLOG_FILENAME_KEY = "canal.binlog.filename";

    //CANAL BINLOG DIR
    public static final String CANAL_BINLOG_DIR_KEY = "canal.binlog.dir";

    //CANAL MQ
    public static final String CANAL_MQ_KEY = "canal.mq";

    public static final String CANAL_PRINT_TRUE = "y";

    /**
     * rabbitMq
     */
    public static final String RABBITMQ_HOST_KEY = "rabbitmq.host";

    public static final String RABBITMQ_PORT_KEY = "rabbitmq.port";

    public static final String RABBITMQ_USER_KEY = "rabbitmq.user";

    public static final String RABBITMQ_PASS_KEY = "rabbitmq.pass";

    public static final String RABBITMQ_QUEUENAME_KEY = "rabbitmq.queuename";

    public static final String RABBITMQ_ACK_KEY = "rabbitmq.ack";

    public static final String RABBITMQ_DURABLE_KEY = "rabbitmq.durable";

    public static final String RABBITMQ_EXCHANGE_TYPE_KEY = "rabbitmq.exchange.type";

    public static final String RABBITMQ_EXCHANGE_NAME_KEY = "rabbitmq.exchange.name";

    public static final String RABBITMQ_ROUTINGKEY_KEY = "rabbitmq.routing.key";

    public static final String RABBITMQ_DURABLE_TRUE = "y";

    /**
     * redis
     */
    public static final String REDIS_HOST_KEY = "redis.host";

    public static final String REDIS_PORT_KEY = "redis.port";

    public static final String REDIS_QUEUENAME_KEY = "redis.queuename";

    /**
     * kafka
     */
    public static final String KAFKA_BOOTSTRAP_SERVERS_KEYS = "kafka.bootstrap.servers";

    public static final String KAFKA_TOPIC_KEYS = "kafka.topic";

    //DEFAULT VALUE === begin
    //CANAL
    /**
     * 默认CANAL服务端HOST
     */
    public static final String DEFAULT_CANAL_SERVER_HOST = "127.0.0.1";

    public static final String DEFAULT_CANAL_SERVER_PORT = "11111";

    public static final String DEFAULT_CANAL_SERVER_INSTANCE = "example";

    public static final String DEFAULT_CANAL_BATCHSIZE = "1000";

    public static final String DEFAULT_CANAL_SLEEP = "1000";

    public static final String DEFAULT_CANAL_PRINT = "y";

    public static final String DEFAULT_CANAL_BINLOG_FILENAME = "d";

    public static final String DEFAULT_CANAL_BINLOG_DIR = System.getProperty("user.dir") + File.separator + "data";

    public static final String DEFAULT_CANAL_MQ = "rabbitmq";

    //RABBITMQ
    /**
     * 默认rabbitmq host
     */
    public static final String DEFAULT_RABBITMQ_HOST = "127.0.0.1";

    public static final String DEFAULT_RABBITMQ_PORT = "5672";

    public static final String DEFAULT_RABBITMQ_USER = "";

    public static final String DEFAULT_RABBITMQ_PASS = "";

    public static final String DEFAULT_RABBITMQ_QUEUENAME = "canal_binlog_data";

    public static final String DEFAULT_RABBITMQ_ACK = "n";

    public static final String DEFAULT_RABBITMQ_DURABLE = "n";

    /**
     * rabbitmq默认直连交换机
     */
    public static final String DEFAULT_RABBITMQ_EXCHANGE_TYPE = "direct";

    public static final String DEFAULT_RABBITMQ_EXCHANGE_NAME = "cfpu_saas";

    public static final String DEFAULT_RABBITMQ_ROUTING_KEY = "";

    //REDIS
    /**
     * 默认redis host
     */
    public static final String DEFAULT_REDIS_HOST = "127.0.0.1";

    /**
     * 默认redis port
     */
    public static final String DEFAULT_REDIS_PORT = "6379";

    public static final String DEFAULT_REDIS_QUEUENAME = "canal_binlog_data";

    /**
     * kafka
     */
    public static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    public static final String DEFAULT_KAFKA_TOPIC = "canal_binlog_data";
    //DEFAULT VALUE === end

    // rabbitmq的交换机类型
    /**
     * direct
     */
    public static final String RABBITMQ_EXCHANGE_TYPE_DIRECT = "direct";

    /**
     * topic
     */
    public static final String RABBITMQ_EXCHAGE_TYPE_TOPIC = "topic";

    /**
     * fanout
     */
    public static final String RABBITMQ_EXCHANGE_TYPE_FANOUT = "fanout";

}
