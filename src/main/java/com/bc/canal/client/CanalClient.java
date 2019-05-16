package com.bc.canal.client;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.bc.canal.client.cons.Constants;
import com.bc.canal.client.parser.DataParser;
import com.bc.canal.client.utils.ConfigUtils;

/**
 * 入口类
 *
 * @author zhou
 */
public class CanalClient {
    private static final Logger logger = Logger.getLogger(CanalClient.class);
    static String canalServerHost;
    static String canalServerPort;
    static String canalServerInstance;
    static String canalBatchSize;
    static String canalSleep;
    /**
     * 是否打印rowData
     * y:打印  n:不打印  默认打印
     */
    public static String canalPrint;
    public static String canalBinlogFilename;
    public static String canalBinlogDir;

    public static String canalMq;

    static int canalServerPortInt;
    static int canalBatchSizeInt;
    static int canalSleepInt;

    /**
     * rabbitmq
     */
    public static String rabbitmqHost;
    public static String rabbitmqPort;
    public static String rabbitmqUser;
    public static String rabbitmqPass;
    public static String rabbitmqQueuename;
    public static String rabbitmqAck;
    public static String rabbitmqDurable;
    public static String rabbitmqExchangeType;
    public static String rabbitmqExchangeName;
    public static String rabbitmqRoutingKey;

    public static int rabbitmqPortInt;

    /**
     * redis
     */
    public static String redisHost;
    public static String redisPort;
    public static String redisQueuename;

    public static int redisPortInt;

    /**
     * kafka
     */
    public static String kafkaAsync;
    public static String kafkaBootstrapServers;
    public static String kafkaTopic;


    public static void main(String[] args) {
        init();
        logger.info("[canal.server] host: " + canalServerHost +
                ", port: " + canalServerPort + ", instance: " + canalServerInstance);

        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalServerHost, canalServerPortInt),
                canalServerInstance, "", "");
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();

            logger.info("connect success! \r\n startup...");
            while (true) {
                Message message = connector.getWithoutAck(canalBatchSizeInt);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (-1 == batchId || 0 == size) {
                    Thread.sleep(canalSleepInt);
                } else {
                    DataParser.parse(message.getEntries());
                }
                connector.ack(batchId);
            }
        } catch (Exception e) {
            logger.error("connect error! msg:" + e.getMessage());
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }

    }

    private static void init() {
        canalServerHost = ConfigUtils.getProperty(Constants.CANAL_SERVER_HOST_KEY,
                Constants.DEFAULT_CANAL_SERVER_HOST);

        canalServerPort = ConfigUtils.getProperty(Constants.CANAL_SERVER_PORT_KEY,
                Constants.DEFAULT_CANAL_SERVER_PORT);
        try {
            canalServerPortInt = Integer.valueOf(canalServerPort);
        } catch (Exception e) {
            logger.warn("Fail to load canal.server.port, it's not Integer, check canal.server.port value:" + canalServerPort);
            canalServerPortInt = Integer.valueOf(Constants.DEFAULT_CANAL_SERVER_PORT);
        }

        canalServerInstance = ConfigUtils.getProperty(Constants.CANAL_SERVER_INSTANCE_KEY,
                Constants.DEFAULT_CANAL_SERVER_INSTANCE);


        canalBatchSize = ConfigUtils.getProperty(Constants.CANAL_BATCHSIZE_KEY,
                Constants.DEFAULT_CANAL_BATCHSIZE);
        try {
            canalBatchSizeInt = Integer.valueOf(canalBatchSize);
        } catch (Exception e) {
            logger.warn("Fail to load canal.batchsize, it's not Integer, check canal.batchsize value:" + canalBatchSize);
            canalBatchSizeInt = Integer.valueOf(Constants.DEFAULT_CANAL_BATCHSIZE);
        }

        canalSleep = ConfigUtils.getProperty(Constants.CANAL_SLEEP_KEY,
                Constants.DEFAULT_CANAL_SLEEP);
        try {
            canalSleepInt = Integer.valueOf(canalSleep);
        } catch (Exception e) {
            logger.warn("Fail to load canal.sleep, it's not Integer, check canal.sleep value:" + canalSleep);
            canalSleepInt = Integer.valueOf(Constants.DEFAULT_CANAL_SLEEP);
        }

        canalPrint = ConfigUtils.getProperty(Constants.CANAL_PRINT_KEY,
                Constants.DEFAULT_CANAL_PRINT);
        canalBinlogFilename = ConfigUtils.getProperty(Constants.CANAL_BINLOG_FILENAME_KEY,
                Constants.DEFAULT_CANAL_BINLOG_FILENAME);
        canalBinlogDir = ConfigUtils.getProperty(Constants.CANAL_BINLOG_DIR_KEY,
                Constants.DEFAULT_CANAL_BINLOG_DIR);

        canalMq = ConfigUtils.getProperty(Constants.CANAL_MQ_KEY,
                Constants.DEFAULT_CANAL_MQ);


        //rabbitmq
        rabbitmqHost = ConfigUtils.getProperty(Constants.RABBITMQ_HOST_KEY,
                Constants.DEFAULT_RABBITMQ_HOST);
        rabbitmqPort = ConfigUtils.getProperty(Constants.RABBITMQ_PORT_KEY,
                Constants.DEFAULT_RABBITMQ_PORT);
        try {
            rabbitmqPortInt = Integer.valueOf(rabbitmqPort);
        } catch (Exception e) {
            logger.warn("Fail to load rabbitmq.port, it's not Integer, check rabbitmq.port value:" + rabbitmqPort);
            rabbitmqPortInt = Integer.valueOf(Constants.DEFAULT_RABBITMQ_PORT);
        }
        rabbitmqUser = ConfigUtils.getProperty(Constants.RABBITMQ_USER_KEY,
                Constants.DEFAULT_RABBITMQ_USER);
        rabbitmqPass = ConfigUtils.getProperty(Constants.RABBITMQ_PASS_KEY,
                Constants.DEFAULT_RABBITMQ_PASS);
        rabbitmqQueuename = ConfigUtils.getProperty(Constants.RABBITMQ_QUEUENAME_KEY,
                Constants.DEFAULT_RABBITMQ_QUEUENAME);
        rabbitmqAck = ConfigUtils.getProperty(Constants.RABBITMQ_ACK_KEY,
                Constants.DEFAULT_RABBITMQ_ACK);
        rabbitmqDurable = ConfigUtils.getProperty(Constants.RABBITMQ_DURABLE_KEY,
                Constants.DEFAULT_RABBITMQ_DURABLE);
        // rabbitmq交换机类型(direct/topic/fanout)
        rabbitmqExchangeType = ConfigUtils.getProperty(Constants.RABBITMQ_EXCHANGE_TYPE_KEY,
                Constants.DEFAULT_RABBITMQ_EXCHANGE_TYPE);
        // 交换器名
        rabbitmqExchangeName = ConfigUtils.getProperty(Constants.RABBITMQ_EXCHANGE_NAME_KEY,
                Constants.DEFAULT_RABBITMQ_EXCHANGE_NAME);
        // routingKey
        rabbitmqRoutingKey = ConfigUtils.getProperty(Constants.RABBITMQ_ROUTINGKEY_KEY,
                Constants.DEFAULT_RABBITMQ_ROUTING_KEY);

        //redis
        redisHost = ConfigUtils.getProperty(Constants.REDIS_HOST_KEY,
                Constants.DEFAULT_REDIS_HOST);
        redisPort = ConfigUtils.getProperty(Constants.REDIS_PORT_KEY,
                Constants.DEFAULT_RABBITMQ_PORT);
        try {
            redisPortInt = Integer.valueOf(redisPort);
        } catch (Exception e) {
            logger.warn("Fail to load redis.port, it's not Integer, check redis.port value:" + redisPort);
            redisPortInt = Integer.valueOf(Constants.DEFAULT_REDIS_PORT);
        }
        redisQueuename = ConfigUtils.getProperty(Constants.REDIS_QUEUENAME_KEY,
                Constants.DEFAULT_REDIS_QUEUENAME);

        //kafka
        kafkaBootstrapServers = ConfigUtils.getProperty(Constants.KAFKA_BOOTSTRAP_SERVERS_KEYS,
                Constants.DEFAULT_KAFKA_BOOTSTRAP_SERVERS);
        kafkaTopic = ConfigUtils.getProperty(Constants.KAFKA_TOPIC_KEYS,
                Constants.DEFAULT_KAFKA_TOPIC);
    }
}
