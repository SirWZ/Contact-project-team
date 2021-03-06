package com.ws.spark2

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

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
  * 创建时间：2018-11-21 16:06
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
object Demo01 {
  def main(args: Array[String]): Unit = {

    val sc = new SparkContext("local", "local")
    val seq = sc.parallelize(1 to 100)

    val str=new String("asdasd")

    seq.foreach(i => println("%s%s",str,i))
    println("count => " + str)

    sc.stop()
  }

}
