package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object Usernames {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = new SparkContext("local", "ParquetReading")

    val sqlContext: SQLContext = new SQLContext(sc)

    val df = sqlContext.read.load("/home/smshafiuddin/work/parquet152/userNames.parquet")

    df.printSchema()
    
    df.show()

  }
}