package com.ws.akka.test.demo02;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.ws.akka.demo02.Device;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 * 类名称：TestDevice
 * </p>
 * <p>
 * 类描述：${DESCRIPTION}
 * </p>
 * <p>
 * 创建人：sun
 * </p>
 * <p>
 * 创建时间：2018-08-31 17:07
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
public class TestDevice {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("TestDevice");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void testReplyWithEmptyReadingIfNoTemperatureIsKnown() {

        TestKit probe = new TestKit(system);

        ActorRef deviceActor = system.actorOf(Device.props("group", "device"),"device");

        deviceActor.tell(new Device.ReadTemperature(42L), probe.getRef());

        Device.RespondTemperature response = probe.expectMsgClass(Device.RespondTemperature.class);

        assertEquals(42L, response.requestId);
        assertEquals(Optional.empty(), response.value);
    }
}
