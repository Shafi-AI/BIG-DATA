package com.test.examples.sql.streamingCassandra

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector._
import org.apache.spark.storage.StorageLevel

object PopulateDataCassandra {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("InsertionLogData")
    conf.set("spark.cassandra.connection.host", "localhost")
    val ssc = new StreamingContext(conf, Seconds(1))
    val lines = ssc.socketTextStream("localhost", 8888, StorageLevel.MEMORY_AND_DISK_SER_2)
    lines.print()
    val transformLog = new LogAnalyzer()
    val seperatedData = lines.flatMap(line => transformLog.transformDataIntoLogTable(new String(line)))
    seperatedData.print()
    val keySpace = "casstream"
    val tableName = "caslog"
    val cassandraClass = new CassandraCreateClass(conf, keySpace, tableName)
    cassandraClass.createTables()
    seperatedData.saveToCassandra(keySpace, tableName, SomeColumns("id", "ip", "client", "user", "datetime", "request", "status", "bytes", "referer", "agent"))
    ssc.start()
    ssc.awaitTermination()
  }

}