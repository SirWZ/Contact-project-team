import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
 * 创建时间：2018-11-14 17:07
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
public class Test {

    @org.junit.Test
    public void test() throws InterruptedException {

        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.put("A");
        queue.put("b");
        queue.put("c");
        queue.put("d");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    String poll = null;
                    try {
                        poll = queue.poll(100, TimeUnit.MICROSECONDS);
                        queue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(poll);

                }

            }
        }).start();

        while(true){
            Thread.sleep(1111);
        }


    }
}
