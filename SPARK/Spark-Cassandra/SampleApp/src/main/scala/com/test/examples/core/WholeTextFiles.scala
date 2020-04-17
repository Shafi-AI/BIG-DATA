package com.test.examples.core

import org.apache.spark.SparkContext

object WholeTextFiles {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = new SparkContext("local", "Whole Text File Example")

    val files = sc.wholeTextFiles("hdfs://localhost:8020/home/smshafiuddin/sqoop-query");

    println(s" Partitions: ", files.partitions.length)

    // files.map((contents) => contents).collect().foreach(println)

    //files.map(contents => contents._1).collect().foreach(println)

    val lines = files.map(fileContents => fileContents._2)

    lines.collect().foreach { println }

    println(" key, value ")
    files.map(contents => (contents._1, contents._2)).collect().foreach(println)
    /*
      * (hdfs://localhost:8020/home/smshafiuddin/sqoop-query/part-m-00000,Abc
Abcd
GUL
GUL1
)
(hdfs://localhost:8020/home/smshafiuddin/sqoop-query/part-m-00001,shafiuddin
)
(hdfs://localhost:8020/home/smshafiuddin/sqoop-query/part-m-00003,GUL
GUL1
)
(hdfs://localhost:8020/home/smshafiuddin/sqoop-query/part-m-00004,shafiuddin
)
(hdfs://localhost:8020/home/smshafiuddin/sqoop-query/part-m-00002,)

      */

  }
}