import akka.actor.ActorSystem
import com.ws.akka.scala.demo02.{Device, DeviceManager}

import scala.concurrent.duration._

/**
  *
  * <p>
  * 类名称：DeviceTest
  * </p>
  * <p>
  * 类描述：${DESCRIPTION}
  * </p>
  * <p>
  * 创建人：sun
  * </p>
  * <p>
  * 创建时间：2018-08-30 8:51
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
class DeviceTest {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("driverTest")
    val deviceActor = system.actorOf(Device.props("group", "device"))


  }

}
