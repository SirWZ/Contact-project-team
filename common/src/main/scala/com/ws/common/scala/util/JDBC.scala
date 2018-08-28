package com.ws.common.scala.util

import java.sql.ResultSet


import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  *
  * <p>
  * 类名称：JDBC
  * </p>
  * <p>
  * 类描述：${DESCRIPTION}
  * </p>
  * <p>
  * 创建人：sun
  * </p>
  * <p>
  * 创建时间：2018-08-28 16:23
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
object JDBC {

  implicit class JDBC(sql: SQL) {

    def executeQuery[A: ClassTag](callBack: ResultSet => A): ArrayBuffer[A] = {
      val instance = JDBCHelper.getInstance
      val result = new ArrayBuffer[A]()
      instance.executeQuery(sql.sql, sql.paramater, rs => result.append(callBack(rs)))
      result
    }

    def executeUpdate: Int = {
      val instance = JDBCHelper.getInstance
      instance.executeUpdate(sql.sql, sql.paramater)
    }

  }

}

case class SQL(sql: String, paramater: Array[AnyRef] = Array[AnyRef]())
