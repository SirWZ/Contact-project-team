package com.ws.akka.scala.demo01

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.io.StdIn

/**
  *
  * <p>
  * 类名称：Demo01
  * </p>
  * <p>
  * 类描述：${DESCRIPTION}
  * </p>
  * <p>
  * 创建人：sun
  * </p>
  * <p>
  * 创建时间：2018-08-29 18:03
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

class Demo01 extends Actor with ActorLogging {


  override def preStart(): Unit = log.info("IoT Application started")

  override def postStop(): Unit = log.info("IoT Application stopped")

  override def receive: Receive = {
    Actor.emptyBehavior
  }
}

object Demo01 {

  def props(): Props = Props(new Demo01)

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("iot-system")

    try {
      // Create top level supervisor
      val supervisor = system.actorOf(Demo01.props(), "iot-supervisor")
      // Exit the system after ENTER is pressed
      StdIn.readLine()
    } finally {
      system.terminate()
    }
  }
}
