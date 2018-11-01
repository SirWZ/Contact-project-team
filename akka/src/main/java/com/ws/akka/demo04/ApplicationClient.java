package com.ws.akka.demo04;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;

/**
 * <p>
 * 类名称：ApplicationClient
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
public class ApplicationClient {
    public static void main(String args[]) {
        ActorSystem system = ActorSystem.create("client", ConfigFactory.load("client"));
        ActorSelection selection = system.actorSelection("akka.tcp://server@127.0.0.1:2552/user/serverActor");
        ActorRef clientActor = system.actorOf(Client.create(), "clientActor");

        selection.tell("asdasdasd", clientActor);


    }
}
