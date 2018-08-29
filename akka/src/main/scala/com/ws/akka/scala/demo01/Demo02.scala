package com.ws.akka.scala.demo01

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
  *
  * <p>
  * 类名称：Demo02
  * </p>
  * <p>
  * 类描述：${DESCRIPTION}
  * </p>
  * <p>
  * 创建人：sun
  * </p>
  * <p>
  * 创建时间：2018-08-29 18:14
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
  *
  */
class Demo02(groupId: String, deviceId: String) extends Actor with ActorLogging {

  import Demo02._

  var lastTemperatureReading: Option[Double] = None

  override def preStart(): Unit = log.info("Device actor {}-{} started", groupId, deviceId)

  override def postStop(): Unit = log.info("Device actor {}-{} stopped", groupId, deviceId)

  override def receive: Receive = {
    case ReadTemperature(id) =>
      log.info("ReadTemperature Id is {} recevie success", id)
      sender() ! RespondTemperature(id, lastTemperatureReading)
  }
}

object Demo02 {

  final case class ReadTemperature(requestId: Long)

  final case class RespondTemperature(requestId: Long, value: Option[Double])

  def props(groupId: String, deviceId: String): Props = Props(new Demo02(groupId, deviceId))

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("iot-system")
    val deviceActor = system.actorOf(Demo02.props("group", "device"))

    deviceActor ! Demo02.ReadTemperature(requestId = 42)

    Thread.sleep(1000)

  }

}
