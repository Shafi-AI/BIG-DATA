package com.test.examples.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext,Seconds}
import org.apache.spark.storage.StorageLevel

object StreamTest {
 
  def main(args: Array[String]): Unit = {
    
    

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("NetworkWordCount")
    
    val ssc  = new StreamingContext(sparkConf, Seconds(1))


    
    val lines = ssc.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
  

    
    val words = lines.flatMap(_.split(" "))

    
    words.foreach((rdd,time) =>
      
      rdd.collect().foreach { println }
        
    )
    val wordCounts = words.map { x =>( x,1) }.reduceByKey(_ + _)
    
    wordCounts.print()
    
    
   wordCounts.saveAsTextFiles("/home/smshafiuddin/work/streamingExamplessayyada/sc.txt")
    
    ssc.start()
    
    ssc.awaitTermination()
    
  }
}