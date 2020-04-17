package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SaveMode

object ParquetExample {

  def main(args: Array[String]): Unit = {

    val sc = new SparkContext("local", "ParquetWriteExample")
    val sqlContext = new SQLContext(sc)

    val parquetFile = sqlContext.read.parquet("users.parquet")

    parquetFile.registerTempTable("pp")
    val pt = sqlContext.sql("select name from pp")

    pt.map { t => "Name: " + t(0) }.collect().foreach { println }
    
    pt.save("/home/smshafiuddin/work/parquet152/userNames.parquet", SaveMode.ErrorIfExists)
    
    
    

  }
}