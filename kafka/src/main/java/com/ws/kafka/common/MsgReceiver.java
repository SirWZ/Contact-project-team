package com.ws.kafka.common;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ProjectName: BigdataProject
 * @Package: com.yssh.bigdata.common.kafka.Consumer
 * @ClassName: Consumer
 * @Description:
 * @Author: Jackie Yang
 * @Date: 2017/7/4
 * @ModifyBy: jk
 * @UpdateUser: jk
 * @UpdateDate: 2017/7/4 14:57
 * @UpdateRemark: 说明本次修改内容
 * @Version: [v1.0]
 * @Copyright: 北京中燕信息技术有限公司
 */
public class MsgReceiver implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MsgReceiver.class);
    private BlockingQueue<Map<TopicPartition, OffsetAndMetadata>> commitQueue = new LinkedBlockingQueue<>();
    private Map<String, Object> consumerConfig;
    private String alarmTopic;
    private ConcurrentHashMap<TopicPartition, RecordProcessor> recordProcessorTasks;
    private ConcurrentHashMap<TopicPartition, Thread> recordProcessorThreads;
    private Class processorClazz;

    public MsgReceiver(Class clazz, Map<String, Object> consumerConfig, String alarmTopic,
                       ConcurrentHashMap<TopicPartition, RecordProcessor> recordProcessorTasks,
                       ConcurrentHashMap<TopicPartition, Thread> recordProcessorThreads) {
        this.consumerConfig = consumerConfig;
        this.alarmTopic = alarmTopic;
        this.recordProcessorTasks = recordProcessorTasks;
        this.recordProcessorThreads = recordProcessorThreads;
        this.processorClazz = clazz;
    }


    @Override
    public void run() {
        //kafka Consumer是非线程安全的,所以需要每个线程建立一个consumer
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig);
        String[] topics = alarmTopic.split(",");
        consumer.subscribe(Arrays.asList(topics), new KafkaConsumerRunner(consumer));
        
    }

    class KafkaConsumerRunner implements ConsumerRebalanceListener {
        private KafkaConsumer<String, String> consumer;
        boolean rewindOffsets = false;

        public KafkaConsumerRunner(KafkaConsumer<String, String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> topicPartitions) {
            logger.debug("onPartitionsRevoked:{}", topicPartitions);
            Iterator iterator = topicPartitions.iterator();
            while (iterator.hasNext()) {
                TopicPartition topicPartition = (TopicPartition) iterator.next();
                long lastOffset = consumer.position(topicPartition);
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(lastOffset);
                Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
                map.put(topicPartition, offsetAndMetadata);
                consumer.commitSync(map);
            }
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            logger.debug("onPartitionsAssigned:{}", partitions);
            try {
                for (TopicPartition partition : partitions) {
                    long offset = 0;
                    OffsetAndMetadata latestCommittedOffsets = consumer.committed(partition);
                    if (latestCommittedOffsets != null) {
                        offset = latestCommittedOffsets.offset();
                    }

                    if (rewindOffsets)
                        offset = rewindOffsets(offset, 100);

                    long lastOffset = consumer.position(partition);

                    System.out.println("Partition:" + partition + " currentOffset:" + offset + " Latest Offset:" + lastOffset);

                    if (offset >= 0) {
                        if (consumer != null) {
                            consumer.seek(partition, offset);
                            //consumer.commitSync();
                        }
                    }
                }

                //seekCompleted = true;
            } catch (Exception e) {
                logger.error("Error when load offset from custom source", e);
            }

        }

        private long rewindOffsets(long currentOffset, long numberOfMessagesToRewindBackTo) {
            return currentOffset - numberOfMessagesToRewindBackTo;
        }
    }
}