package com.test.examples.sql.cassandraTables

import org.apache.spark._
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark.rdd.RDD



object PartitionGroupingExample {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local").setAppName("Join Operation for Cassandra Tables")

    sparkConf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(sparkConf)

    
    val visits =  sparkContext.cassandraTable[(String)]("shafi_join_test", "user_visits").select("user")
    
    val visitsPerUser =  visits.map { user => (user,1) }.mapPartitions{
    
    userIterator => new GroupByKeyIterator(userIterator)
    }.mapValues(_.size)
    
    val maxVisits = visitsPerUser.values.max
    
    println(s"max Visists",maxVisits)
    
    

  
  
  
  
  
  
  }
}