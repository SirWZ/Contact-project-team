package com.ws.kafka;

import com.ws.kafka.common.MainKafkaProcessor;
import com.ws.kafka.executor.Test;
import kafka.server.KafkaApis;
import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * <p>
 * 类名称：Application
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-08 13:38
 * </p>
 * <p>
 * 修改人：
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修改备注：
 * </p>
 * <p>
 * Copyright (c) 版权所有
 * </p>
 *
 * @version 1.0.0
 */
public class Application {

    public static void main(String argsp[]) throws ClassNotFoundException {

        MainKafkaProcessor kafkaProcessor = new MainKafkaProcessor();

        kafkaProcessor.setServers("10.101.127.166:2181");
        kafkaProcessor.setAlarmTopic("send_message");
        kafkaProcessor.setGroup("testasdasdasd");
        Class<?> clazz = Class.forName(Test.class.getName());
        kafkaProcessor.setProcessorClazz(clazz);
        kafkaProcessor.setThreadNum(1);
        kafkaProcessor.main();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
