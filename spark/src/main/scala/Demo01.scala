import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

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
  * 创建时间：2018-09-11 15:28
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
    Logger.getRootLogger.setLevel(Level.ERROR)
    val sc = new SparkContext("local[2]", "local")
    val ssc = new StreamingContext(sc, Seconds(5))

    val ds = ssc.socketTextStream("10.101.127.185", 9999)
    val unit = ds.flatMap(_.split(" ")).map(s => (s, 1)).reduceByKey(_ + _)
    val windwo = unit.window(Duration(10000), Duration(5000))
    windwo.foreachRDD(rdd => println("RDD ID"+rdd.id))

    windwo.transform {
      rdd =>
        rdd.sortBy(_._2)
    }.print()
    ssc.start()
    ssc.awaitTermination()


  }

}
