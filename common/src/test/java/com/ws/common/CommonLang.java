package com.ws.common;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * <p>
 * ClassName：CommonLang
 * </p>
 * <p>
 * Description：${DESCRIPTION}
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-04-24 8:58
 * </p>
 * <p>
 * Modify：
 * </p>
 * <p>
 * ModifyTime：
 * </p>
 * <p>
 * Commont：
 * </p>
 * <p>
 * Copyright (c)
 * </p>
 *
 * @author sun
 * @version 1.0.0
 */
public class CommonLang {

    @Test
    public void test1() throws InterruptedException {

        StopWatch watch = new StopWatch();
        watch.start();

        Thread.sleep(3000);

        watch.stop();

        System.out.println(watch.getStartTime());
        System.out.println(watch.getTime());



    }
}
