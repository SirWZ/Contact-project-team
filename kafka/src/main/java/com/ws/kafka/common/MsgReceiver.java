package com.ws.kafka.common;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


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

        /*
        TopicPartition topicPartition1 = new TopicPartition(topics[0], 2);
        consumer.assign(Arrays.asList(topicPartition1));
        consumer.seek(topicPartition1, 0);
        long offset = consumer.position(topicPartition1);
        System.out.println(offset);*/

        //consumer.seekToEnd(topicPartition1);
        consumer.subscribe(Arrays.asList(topics),
                new ConsumerRebalanceListener() {
                    boolean rewindOffsets = false;

                    //消费者撤出时调用
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

                    //重新平衡之后调用
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

                    private long rewindOffsets(long currentOffset,
                                               long numberOfMessagesToRewindBackTo) {
                        return currentOffset - numberOfMessagesToRewindBackTo;
                    }
                });
        //检查线程中断标志是否设置, 如果设置则表示外界想要停止该任务,终止该任务
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //查看该消费者是否有需要提交的偏移信息, 使用非阻塞读取
                    Map<TopicPartition, OffsetAndMetadata> toCommit = commitQueue.poll();
                    if (toCommit != null) {
                        logger.debug("commit TopicPartition offset to kafka: " + toCommit);
                        consumer.commitSync(toCommit);
                    }
                    //最多轮询100ms
                    ConsumerRecords<String, String> records = consumer.poll(100);
                    if (records.count() > 0) {
                        logger.debug("poll records size: " + records.count());
                    }
                    for (TopicPartition topicPartition : records.partitions()) {
                        RecordProcessor processTask = recordProcessorTasks.get(topicPartition);
                        //如果当前分区还没有开始消费, 则就没有消费任务在map中
                        if (processTask == null) {
                            //生成新的处理任务和线程, 然后将其放入对应的map中进行保存
                            processTask = new RecordProcessor(commitQueue, processorClazz);
                            recordProcessorTasks.put(topicPartition, processTask);
                            Thread thread = new Thread(processTask);
                            thread.setName("Thread-for " + topicPartition.toString());
                            logger.info("start Thread: " + thread.getName());
                            thread.start();
                            recordProcessorThreads.put(topicPartition, thread);
                        }
                        //将消息放到处理队列
                        List<ConsumerRecord<String, String>> recordList = records.records(topicPartition);
                        processTask.addRecordToQueue(recordList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("MsgReceiver exception " + e + " ignore it");
                }
            }
        } finally {
            consumer.close();
        }
    }

}
