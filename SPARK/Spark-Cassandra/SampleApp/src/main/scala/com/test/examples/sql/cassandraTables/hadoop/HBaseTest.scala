package com.test.examples.sql.cassandraTables.hadoop

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.HConstants
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.mapreduce.OutputFormat
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.client.Result
import org.apache.spark.SparkConf
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat

object HBaseTest extends Serializable {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("HBASE TEST").setMaster("local[*]")

    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    //conf.registerKryoClasses(Array(classOf[org.apache.hadoop.hbase.client.Result]))
    sparkConf.registerKryoClasses(Array(classOf[org.apache.hadoop.hbase.client.Result]))

    val sc = new SparkContext(sparkConf)

    val conf = HBaseConfiguration.create();

    val tableName = "t1"
    conf.set(HConstants.ZOOKEEPER_QUORUM, "localhost")
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2182")
    conf.set("hbase.defaults.version.skip", "true")
    conf.set(TableInputFormat.INPUT_TABLE, tableName) //  working to here 
    //conf.set("mapreduce.job.output.format.class",classOf[TableInputFormat],classOf[OutputFormat[String, Mut,)

    val admin = new HBaseAdmin(conf)

    if (admin.isTableAvailable(tableName)) {
      println("table is available ")
      // conf.set(TableInputFormat.SCAN_COLUMNS, "CF:" + "c1", "CF:" + "cf")
      conf.set(TableInputFormat.SCAN_COLUMNS,  "c1" )
      

      val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

      val  check  = hbaseRDD.map {
        case (k, r) => {

          val rowKey = Bytes.toString(k.get())
          val value = Bytes.toString(r.getValue(Bytes.toBytes("c1"), Bytes.toBytes("b1")))
          (rowKey, value)

        }
      }

      check.collect().foreach(println)
      /*
      
      hbaseRDD.map(x => x._2).map(_.list).collect.foreach(println)
      
      val resultRDD =  hbaseRDD.map(tuple => tuple._2)
      val keyValueRDD =  resultRDD.map { result => (Bytes.toString(result.getRow()).split(" ")(0), Bytes.toDouble(result.value())) }
      keyValueRDD.collect.foreach(println) 
      */
      //.. val keyValueRDD =  hbaseRDD.map(result => (Bytes.toString(result._1)  )

      println(s"rows: ", hbaseRDD.count())
    }
    
    //  Insert New Data 
    
    val data  = sc.parallelize(List(("r4","sss"),("r5","sss1"),("r6","sss2")))
    
     data.collect.foreach(println) 
    
    val resultData  =data.map( value => (new ImmutableBytesWritable,{
      
      val rec =  new Put(Bytes.toBytes(value._1));
      rec.add(Bytes.toBytes("c1"), Bytes.toBytes("b1"),Bytes.toBytes(value._2.toString()));
      rec
    }))
    
    conf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    conf.set("mapreduce.output.fileoutputformat.outputdir","hdfs://localhost:8020/home/smshafiuddin/work/user")
    
    resultData.saveAsNewAPIHadoopDataset(conf)
    
    

  }
}