package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object JSonExample {

  def main(args: Array[String]): Unit = {

    val sc: SparkContext = new SparkContext("local", "JSonReaderExample")

    val sqlContext: SQLContext = new SQLContext(sc)

    val df = sqlContext.read.format("json").load("people.json")

    df.registerTempTable("people")

    val cols = sqlContext.sql("select name, age from people")

    cols.map { person => "Name: " + person(0) }.collect().foreach { println }

    cols.save("/home/smshafiuddin/work/parquet152/json/jsonwrite.json")
  }
}