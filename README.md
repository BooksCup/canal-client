# canal-client
阿里巴巴数据同步组件canal的客户端,解析binlog增量数据后发送至MQ(rabbitmq,kafka,redis),后续会支持activemq和rocketmq  
使用场景:异步同步业务数据至多端,比如可以监听商品库,解析binlog增量数据,发送商品改动数据(增删改)至MQ的某个topic中,在多个数据源中订阅这个topic,更新多个数据源的数据,比如写入es集群和分布式缓存架构中  

## 各种MQ的介绍和接入说明:
### rabbitMQ
    根据业务场景选择交换机的类型
    P2P(点对点)场景可以使用direct,需要配置交换机类型为direct,交换机名称可以不配置
    canal.properties中配置:
    rabbitmq.exchange.type = direct
    rabbitmq.exchange.name = 
    PUB/SUB(发布/订阅)场景可以使用topic或者fanout
    
