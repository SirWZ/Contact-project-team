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
//    Logger.getRootLogger.setLevel(Level.ERROR)
    val logger = Logger.getLogger("aaa")
    val sc = new SparkContext("local[2]", "local")
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    println(rdd.count())

    logger.warn("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    logger.error("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    logger.fatal("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

  }

}
