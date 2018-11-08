import java.util.concurrent.*;

/**
 * <p>
 * 类名称：Demo
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-11-05 11:24
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
public class Demo1 {
    public static void main(String args[]) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; !Thread.currentThread().isInterrupted() && i <= 30; i++) {
            executorService.execute(new MyTask(i));
        }

        System.out.println("执行完毕");

    }

    static class MyTask implements Runnable {
        private int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task " + taskNum);
            try {
                Thread.currentThread().sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + taskNum + "执行完毕");
        }
    }
}
