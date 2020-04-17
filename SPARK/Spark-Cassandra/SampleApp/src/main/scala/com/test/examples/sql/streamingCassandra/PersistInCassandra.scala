package com.test.examples.sql.streamingCassandra

import scala.reflect.runtime.universe

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import com.datastax.spark.connector._
import com.datastax.spark.connector.streaming._


object PersistInCassandra {

  def main(args: Array[String]): Unit = {

    println("Creating Spark Configuration ")

   //  val conf = new SparkConf().setAppName("PersistInCassandra").setMaster("local[*]")

    //val conf = new SparkConf()
    
    //conf.set("spark.cassandra.connection.host", "localhost")

    
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("PersistInCassandraExample")
    
    val streamingContext  = new StreamingContext(sparkConf, Seconds(1))
    
    val data = streamingContext.socketTextStream("localhost", 9999, StorageLevel.MEMORY_AND_DISK_SER)
    
    data.print
    
    val transformLog = new LogAnalyzer()

    val newDataStream = data.map { line =>
      {
        println(line);
        //  transformLog.transformLogDataNew(line) 
        val matcher = transformLog.transformLogDataNew(line);

        if (matcher.matches()) transformLog.createDaMap(matcher)
      }
    }

    newDataStream.print()
    
    val keySpace = "STREAM_CASN"

    val tableName = "LOG_DATA"

    // val cassandraClass: CassandraCreateClass = new CassandraCreateClass(sparkContext, conf, keySpace, tableName)

    // cassandraClass.createTables()

    // streamingContext.checkpoint("/home/smshafiuddin/checkpointstream")

   
    
   //  newDataStream.saveToCassandra(keySpace, tableName, SomeColumns("id", "ip", "client", "user", "datetime", "request", "status", "bytes", "referer", "agent"))
    //newDataStream.saveToCassandra(keySpace, tableName, SomeColumns("id","ip", "client", "user","request", "status"))
    /*
     ("id", scala.util.Random.nextInt(76080).toString),
      ("IP" -> m.group(1)),
      ("client" -> m.group(2)),
      ("user" -> m.group(3)),
      ("dateTime" -> m.group(4)),
      ("request" -> m.group(5)),
      ("status" -> m.group(6)),
      ("bytes" -> m.group(7)),
      ("referer" -> m.group(8)),
      ("agent" -> m.group(9)))*/

    streamingContext.start()
    
    streamingContext.awaitTermination()
    
    

    
  }

}

        
        
    
 