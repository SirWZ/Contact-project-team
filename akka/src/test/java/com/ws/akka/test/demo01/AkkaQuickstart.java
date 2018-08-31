package com.ws.akka.test.demo01;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ws.akka.demo01.Greeter;
import com.ws.akka.demo01.Printer;
import org.junit.Test;

import java.io.IOException;

/**
 * <p>
 * 类名称：AkkaQuickstart
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-31 14:55
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
public class AkkaQuickstart {
    @Test
    public void main() {
        ActorSystem system = ActorSystem.create("firstActorSystem");

        ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");

        ActorRef howdyGreeter = system.actorOf(Greeter.props("howdy", printerActor), "howdyGreeter");

        howdyGreeter.tell(new Greeter.WhoToGreet("Akka"), ActorRef.noSender());

        howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

        System.out.println(">>> Press ENTER to exit <<<");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
