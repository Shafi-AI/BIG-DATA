package com.test.examples.sql.cassandraTables

import org.apache.spark._
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark.rdd.RDD

object BroadCastJoinExample {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local").setAppName("Broad Cast for Cassandra Tables")

    sparkConf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(sparkConf)

    val storeToCity = sparkContext.cassandraTable[(String, String)]("shafi_join_test", "stores")
      .select("store", "city").collect().toMap

    val cityOf = sparkContext.broadcast(storeToCity)
    

    //storeToCity.keys.iterator.take(10).foreach { println }
    
    
    //storeToCity.values.iterator.take(10).foreach { println }
    
    
    val visits = sparkContext.cassandraTable("shafi_join_test", "user_visits").select("store", "user")
    
   /*val numberOfRows =  visits.map { row => (row.getString(0),row.getString(1)) }
    
    val visistsPerCity =  numberOfRows.map{ case(store,user) => (cityOf.value.apply(store),1) }.reduceByKey(_ + _)
    
    visistsPerCity.collect().foreach(println)
    */
    
     val visitUsers = sparkContext.cassandraTable[(String,String)]("shafi_join_test", "user_visits").select("store", "user")
   
     val visitorsPerCity = visitUsers.map{case (store,user) => (cityOf.value.apply(store),1 )}.reduceByKey(_ + _)
     
     visitorsPerCity.collect.foreach(println)
     
     println("no of visitors per city",visitorsPerCity.collect.size)
    //visistsPerCity.collect.foreach(println) 
    // visits.collect().foreach { println }

    //val visistsPerCity = visits.map { (store, user) => (cityOf.value.apply(store), 1) }.reduceByKey(_ + _)

     //visistsPerCity.collect.foreach(println)

     
  }
  
}

