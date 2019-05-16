# canal-client
阿里巴巴数据同步组件canal的客户端,解析binlog增量数据后发送至MQ(rabbitmq,kafka,redis),后续会支持activemq和rocketmq  
使用场景:异步同步业务数据至多端,比如可以监听商品库,解析binlog增量数据,发送商品改动数据(增删改)至MQ的某个topic中,在多个数据源中订阅这个topic,更新多个数据源的数据,比如写入es集群和分布式缓存架构中
