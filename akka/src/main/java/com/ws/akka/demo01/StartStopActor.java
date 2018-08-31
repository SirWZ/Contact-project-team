package com.ws.akka.demo01;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * <p>
 * 类名称：StartStopActor
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-31 16:03
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
public class StartStopActor extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        System.out.println("启动第一个actor");
        getContext().actorOf(Props.create(StartStopActor1.class));
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("停止第一个actpr");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("stop", s -> {
            getContext().stop(getSelf());
        }).build();
    }
}

class StartStopActor1 extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        System.out.println("第二个Actor启动");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("第二个Actor 停止!");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
