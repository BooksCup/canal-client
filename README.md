# canal-client
阿里巴巴数据同步组件canal的客户端,解析binlog增量数据后发送至MQ(rabbitmq,kafka,redis),后续会支持activemq和rocketmq  
使用场景:异步同步业务数据至多端,比如可以监听商品库,解析binlog增量数据,发送商品改动数据(增删改)至MQ的某个topic中,在多个数据源中订阅这个topic,更新多个数据源的数据,比如写入es集群和分布式缓存架构中  

## 各种MQ的介绍和接入说明:
### `rabbitMQ:`  
    根据业务场景选择交换机的类型:  
   #### direct
    P2P(点对点)场景可以使用direct,需要配置交换机类型为direct,
    发送消息时会将消息中的RoutingKey与该Exchange关联的所有Binding中的BindingKey进行比较，如果相等，则发送到该Binding对应的Queue中.
    交换机名称和routingKey可以不配置.
    如果这样,消息会被发送到默认的RabbitMQ自带的Exchange：default Exchange,默认的routingKey则为rabbitmq.queuename.
    canal.properties中配置:
    rabbitmq.queuename = canal_binlog_data
    rabbitmq.exchange.type = direct
    rabbitmq.exchange.name = 
    rabbitmq.routing.key = 
   
   #### topic
    PUB/SUB(发布/订阅)场景可以使用topic,需要配置交换机类型为topic
    发送消息时会按照正则表达式，对RoutingKey与BindingKey进行匹配，如果匹配成功，则发送到对应的Queue中.
    交换机名称和routingKey必须配置.
    canal.properties中配置:
    rabbitmq.queuename = canal_binlog_data
    rabbitmq.exchange.type = topic
    rabbitmq.exchange.name = wd.basic.data.sync.exchange
    rabbitmq.routing.key = wd.search.v1.*
    
   #### fanout
    广播场景可以使用fanout,需要配置交换机类型为fanout
    发送消息时会将消息发送给所有与该Exchange定义过Binding的所有Queues中去.
    交换机名称必须配置,routingKey无需配置.
    rabbitmq.queuename = canal_binlog_data
    rabbitmq.exchange.type = fanout
    rabbitmq.exchange.name = wd.basic.data.sync.exchange
    rabbitmq.routing.key = 
    

### `redis:`  
    使用的redis数据类型:列表类型(list)
    使用的redis函数:rpush(生产者),lpop(消费者)
    需要配置redis的ip,端口和队列名,如果redis服务有密码需要配置password,没有则无需配置
    redis.host = 127.0.0.1
    redis.port = 6379
    redis.password = 
    redis.queuename = canal_binlog_data
    
