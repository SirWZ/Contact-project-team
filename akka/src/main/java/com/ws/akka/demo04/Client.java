package com.ws.akka.demo04;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * <p>
 * 类名称：Client
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-09-03 10:48
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
public class Client extends AbstractActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public static Props create() {
        return Props.create(Client.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, s -> {

            logger.error("{}", s);
        }).build();
    }
}
