package com.test.examples.sql.cassandraTables

import org.apache.spark._
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark.rdd.RDD
object JoinOperation {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local").setAppName("Join Operation for Cassandra Tables")

    sparkConf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(sparkConf)

    val stores = sparkContext.cassandraTable[(String,String)]("shafi_join_test", "stores").select("store", "city")

    val visits = sparkContext.cassandraTable[(String,String)]("shafi_join_test", "user_visits").select("store", "user")

    val vistsPerUser = visits.join(stores).map {
      
    case (store,(user,city)) => (city, 1)
    }.reduceByKey(_+_)
    
    
    
    vistsPerUser.foreach(println)

  }

}