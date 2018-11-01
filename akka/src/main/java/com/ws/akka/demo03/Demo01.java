package com.ws.akka.demo03;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

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
 * 创建时间：2018-09-03 9:22
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
public class Demo01 extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props create() {
        return Props.create(Demo01.class);
    }

    @Override
    public void preStart() throws Exception {
        log.info("Server actor init ");
    }

    @Override
    public void postStop() throws Exception {
        log.info("Server actor stop ");
        getContext().stop(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, s -> {
            log.info("revice string is {}", s);
        })
                .matchAny(s -> {
                    log.info("revice any message is {}", s);
                })
                .build();
    }
}
