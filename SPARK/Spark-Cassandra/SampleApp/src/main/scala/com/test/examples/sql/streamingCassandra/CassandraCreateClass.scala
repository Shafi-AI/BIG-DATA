package com.test.examples.sql.streamingCassandra

import org.apache.spark.SparkContext

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import com.datastax.spark.connector.cql.CassandraConnector
import scala.util.Random
import org.apache.spark.rdd.RDD

class CassandraCreateClass(conf: SparkConf, keySpace: String, tableName: String) {
    
  def createTables() = {
    println("Starting CassandraCreateClass.createTables()")
    val dropTable = s"DROP TABLE IF EXISTS  $keySpace.$tableName"
    val dropKeySpace = s"DROP KEYSPACE IF EXISTS  $keySpace" 
    val createKeySpace =s"CREATE KEYSPACE IF NOT EXISTS $keySpace WITH REPLICATION ={'class':'SimpleStrategy','replication_factor': 1}"
    val createTable =s"CREATE TABLE $keySpace.$tableName(ID INT PRIMARY KEY, IP TEXT, CLIENT TEXT,USER TEXT, DATETIME TEXT, REQUEST TEXT, STATUS TEXT, BYTES TEXT, REFERER TEXT, AGENT TEXT)"
    
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(dropTable)
      session.execute(dropKeySpace)
//    session.execute("CREATE KEYSPACE IF NOT EXISTS STREAMCASN WITH REPLICATION ={'class':'SimpleStrategy','replication_factor': 1}")
//    session.execute("CREATE TABLE STREAMCASN.LOG_DATA(ID INT PRIMARY KEY, IP TEXT, CLIENT TEXT,USER TEXT, DATETIME TEXT, REQUEST TEXT, STATUS TEXT, BYTES TEXT, REFERER TEXT, AGENT TEXT)")
      session.execute(createKeySpace)
      session.execute(createTable)
      
    }
    println("Ending CassandraCreateClass.createTables()")
  }
} 