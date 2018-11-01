package com.ws.akka.test.demo03;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.junit.Test;

import java.util.concurrent.Executor;

/**
 * <p>
 * 类名称：Server
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-09-03 9:23
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
public class Demo01 {

    @Test
    public void test01() {
        ActorSystem system = ActorSystem.create("demo01");
        ActorRef actorOf = system.actorOf(com.ws.akka.demo03.Demo01.create(), "actorRefByDemo01");


    }
}
