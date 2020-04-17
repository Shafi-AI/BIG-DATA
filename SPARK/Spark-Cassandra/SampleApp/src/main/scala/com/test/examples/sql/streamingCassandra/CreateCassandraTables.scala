package com.test.examples.sql.streamingCassandra


import org.apache.spark.SparkConf
import com.datastax.spark.connector.cql.CassandraConnector

class CreateCassandraTables(conf: SparkConf, keySpace: String, tableName: String) {

  def createTables() = {
    println("Starting CassandraCreateClass.createTables()")
    val dropKeySpace = s"DROP KEYSPACE IF EXISTS  $keySpace"
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(dropKeySpace)
      session.execute("CREATE KEYSPACE IF NOT EXISTS STREAMCASN WITH REPLICATION ={'class':'SimpleStrategy','replication_factor': 1}")
      session.execute("DROP TABLE IF EXISTS STREAMCASN.LOG_DATA")
      session.execute("CREATE TABLE STREAMCASN.LOG_DATA(ID INT PRIMARY KEY, IP TEXT, CLIENT TEXT,USER TEXT, DATETIME TEXT, REQUEST TEXT, STATUS TEXT, BYTES TEXT, REFERER TEXT, AGENT TEXT)")
    }
    println("Ending CassandraCreateClass.createTables()")
  }
} 
