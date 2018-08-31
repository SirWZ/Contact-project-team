package com.ws.akka.test.demo01;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ws.akka.demo01.SupervisingActor;
import org.junit.Test;

/**
 * <p>
 * 类名称：SupervisingActorTest
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-31 16:12
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
public class SupervisingActorTest {
    @Test
    public void test() throws InterruptedException {
        ActorSystem system = ActorSystem.create();
        ActorRef supervisingActor = system.actorOf(Props.create(SupervisingActor.class), "supervising-actor");
        supervisingActor.tell("failChild", ActorRef.noSender());

        Thread.sleep(3000);
        supervisingActor.tell("stop", ActorRef.noSender());


    }
}
