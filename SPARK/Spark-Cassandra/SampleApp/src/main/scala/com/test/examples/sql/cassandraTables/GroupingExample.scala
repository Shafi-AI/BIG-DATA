package com.test.examples.sql.cassandraTables

import org.apache.spark._
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

object GroupingExample {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local").setAppName("Join Operation for Cassandra Tables")

    sparkConf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(sparkConf)

    
    val visits  = sparkContext.cassandraTable[(String)]("shafi_join_test", "user_visits").select("user")
    
    
    val visitsPerUser  = visits.map { user => (user,1) }.reduceByKey( _ + _) 
    
    println(s"Max Visists", visitsPerUser.values.max)
    
   
    val sqlContext  = new SQLContext(sparkContext)
    val userFrame =  sqlContext.read.format("org.apache.spark.sql.cassandra").options(Map("table"->"user_visits","keyspace" -> "shafi_join_test")).load
  
   
    userFrame.show()
  }
}

