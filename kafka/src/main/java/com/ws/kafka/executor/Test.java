package com.ws.kafka.executor;

import com.ws.kafka.common.IProcessorHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

/**
 * <p>
 * 类名称：Test
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-08 13:44
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
public class Test implements IProcessorHandler {
    @Override
    public void process(Object record) {
        List<ConsumerRecord<String, String>> list = (List<ConsumerRecord<String, String>>) record;
        list.forEach(consumer -> {
            String key = consumer.key();
            String value = consumer.value();
            System.out.println(key + value);
        });
    }
}
