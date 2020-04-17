package com.test.examples.sql

import org.apache.spark.SparkConf
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

object CountHowManyRecords {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("Testing with CassandraSQLContext")

    conf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(conf)

    val stores = sparkContext.cassandraTable("shafi_join_test", "stores")
    println(s"Total No of stores: " + stores.collect().length)

    val userAndVisits = sparkContext.cassandraTable("shafi_join_test", "user_visits")
    println(s"Total No of User Per Visits: " + userAndVisits.collect.length)

  }
}