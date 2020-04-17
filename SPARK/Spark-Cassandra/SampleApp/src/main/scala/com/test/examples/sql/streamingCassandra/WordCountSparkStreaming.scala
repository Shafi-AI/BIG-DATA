package com.test.examples.sql.streamingCassandra

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector._

object WordCountSparkStreaming {
 
  def main(args: Array[String]): Unit = {
    
    val conf =  new SparkConf().setMaster("local[*]").setAppName("Word Count Problem")
    conf.set("spark.cassandra.connection.host", "localhost")
    val ssc = new StreamingContext(conf, Seconds(1))
    val lines  =  ssc.socketTextStream("localhost", 8888)
    val words =  lines.flatMap { _.split(" ") }
    val pairs =  words.map { word => (word,1) }
    
    val wordcounts =  pairs.reduceByKey(_ + _)
    
    
    wordcounts.saveToCassandra("stream_test", "words_table", SomeColumns("word","count"))
    
    
    val table =  ssc.cassandraTable("stream_test", "words_table")
    
    table.select("word","count").collect.foreach(println)
    
    
    ssc.start()
    ssc.awaitTermination()
    
    
  }
}