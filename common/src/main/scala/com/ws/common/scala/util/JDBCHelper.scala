package com.ws.common.scala.util

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util.Properties
import com.alibaba.druid.pool.{DruidDataSource, DruidPooledConnection}
import scala.util.Try

/**
  *
  * <p>
  * 类名称：JDBCHelper
  * </p>
  * <p>
  * 类描述：${DESCRIPTION}
  * </p>
  * <p>
  * 创建人：sun
  * </p>
  * <p>
  * 创建时间：2018-08-28 16:36
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
class JDBCHelper private(var maxActiveSize: Int) {
  private val dataSource = new DruidDataSource
  private val properties = new Properties
  private val initSize: Int = 5
  properties.load(this.getClass.getClassLoader.getResourceAsStream("conf.properties"))
  maxActiveSize = Math.max(maxActiveSize, initSize)

  private  val url: String = properties.getProperty("jdbc.url")
  private val user: String = properties.getProperty("jdbc.user")
  private val password: String = properties.getProperty("jdbc.password")
  private val driver: String = properties.getProperty("jdbc.driver")

  dataSource.setDriverClassName(driver)
  dataSource.setUsername(user)
  dataSource.setPassword(password)
  dataSource.setUrl(url)
  dataSource.setInitialSize(initSize)
  dataSource.setMinIdle(1)
  dataSource.setMaxActive(maxActiveSize)
  dataSource.setRemoveAbandoned(true)
  dataSource.setRemoveAbandonedTimeout(180)
  dataSource.setLogAbandoned(true)
  dataSource.setFilters("stat")
  dataSource.setPoolPreparedStatements(false)

  def closeAll(): Unit = dataSource.close()

  def closeConnection(conn: Connection): Unit = if (conn != null) conn.close()

  private def getConnection: DruidPooledConnection = dataSource.getConnection

  def executeQuery(sql: String, params: Array[AnyRef], callback: ResultSet => Unit): Unit = {
    var conn: DruidPooledConnection = getConnection
    var pstmt: PreparedStatement = conn.prepareStatement(sql)
    params.zipWithIndex.foreach { case (parameter, i) => pstmt.setObject(i + 1, parameter) }
    Try(callback(pstmt.executeQuery)) {
      case _ => pstmt.close()
    }

  }

  def executeUpdate(sql: String, params: Array[AnyRef]): Int = {
    var rtn = 0
    var conn = getConnection
    conn.setAutoCommit(false)
    var pstmt = conn.prepareStatement(sql)
    params.zipWithIndex.foreach { case (parameter, i) => pstmt.setObject(i + 1, parameter) }
    rtn = pstmt.executeUpdate
    conn.commit()
    rtn
  }


  def executeBatch(sql: String, paramsList: Array[Array[AnyRef]]): Array[Int] = {
    var conn = getConnection
    conn.setAutoCommit(false)
    var pstmt = conn.prepareStatement(sql)

    paramsList.foreach {
      params =>
        params.zipWithIndex.foreach { case (parameter, i) => pstmt.setObject(i + 1, parameter) }
        pstmt.addBatch()
    }

    val rtn = pstmt.executeBatch
    conn.commit()
    rtn
  }
}

object JDBCHelper {
  var jdbcHelper: Option[JDBCHelper] = None

  def getInstance: JDBCHelper = {
    synchronized {
      if (jdbcHelper.isEmpty)
        jdbcHelper = Some(new JDBCHelper(5))
      jdbcHelper.get
    }
  }

}
