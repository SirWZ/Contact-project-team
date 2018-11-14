package com.ws.kafka.common;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: BigdataProject
 * @Package: com.yssh.bigdata.common.kafka.MainKafkaProcessor
 * @ClassName: MainKafkaProcessor
 * @Description:
 * @Author: Jackie Yang
 * @Date: 2017/7/6
 * @ModifyBy: jk
 * @UpdateUser: jk
 * @UpdateDate: 2017/7/6 20:35
 * @UpdateRemark: 说明本次修改内容
 * @Version: [v1.0]
 * @Copyright: 北京中燕信息技术有限公司
 */
public class MainKafkaProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MainKafkaProcessor.class);
    //订阅的topic
    private String alarmTopic;
    //brokers地址
    private String servers;
    //消费group
    private String group;
    //开启线程数量对应分区
    private int threadNum;
    //具体执行的类
    private Class processorClazz;


    //kafka消费者配置
    private Map<String, Object> consumerConfig;
    private Thread[] threads;
    //保存处理任务和线程的map
    private ConcurrentHashMap<TopicPartition, RecordProcessor> recordProcessorTasks = new ConcurrentHashMap<>();
    private ConcurrentHashMap<TopicPartition, Thread> recordProcessorThreads = new ConcurrentHashMap<>();

    public void main() {
        consumerConfig = getConsumerConfig();
        logger.debug("get kafka consumerConfig: " + consumerConfig.toString());
        int threadsNum = threadNum;
        logger.debug("create " + threadsNum + " threads to consume kafka warn msg");
        threads = new Thread[threadsNum];
        for (int i = 0; i < threadsNum; i++) {

            MsgReceiver msgReceiver = new MsgReceiver(processorClazz, consumerConfig, alarmTopic, recordProcessorTasks, recordProcessorThreads);
            Thread thread = new Thread(msgReceiver);
            threads[i] = thread;
            thread.setName("alarm msg consumer " + i);
        }
        //启动这几个线程
        for (int i = 0; i < threadsNum; i++) {
            threads[i].start();
        }
        logger.debug("finish creating" + threadsNum + " threads to consume kafka warn msg");
    }

    //销毁启动的线程
    public void destroy() {
        closeRecordProcessThreads();
        closeKafkaConsumer();
    }

    private void closeRecordProcessThreads() {
        logger.debug("start to interrupt record process threads");
        for (Map.Entry<TopicPartition, Thread> entry : recordProcessorThreads.entrySet()) {
            Thread thread = entry.getValue();
            thread.interrupt();
        }
        logger.debug("finish interrupting record process threads");
    }

    private void closeKafkaConsumer() {
        logger.debug("start to interrupt kafka consumer threads");
        //使用interrupt中断线程, 在线程的执行方法中已经设置了响应中断信号
        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
        logger.debug("finish interrupting consumer threads");
    }

    //kafka consumer配置
    private Map<String, Object> getConsumerConfig() {
        return ImmutableMap.<String, Object>builder()
                .put("bootstrap.servers", servers)
                .put("group.id", group)
                .put("enable.auto.commit", "false")
                .put("session.timeout.ms", "30000")
                .put("request.timeout.ms", "70000")
                .put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
                .put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
                .put("max.poll.records", 1000)
                .put("max.poll.interval.ms", 300000)
//                .put("auto.offset.reset", "earliest")
                .put("auto.offset.reset", "latest")
                .put("max.partition.fetch.bytes", 100 * 1024)
                .build();
    }

    public void setAlarmTopic(String alarmTopic) {
        this.alarmTopic = alarmTopic;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public void setProcessorClazz(Class processorClazz) {
        this.processorClazz = processorClazz;
    }


}
