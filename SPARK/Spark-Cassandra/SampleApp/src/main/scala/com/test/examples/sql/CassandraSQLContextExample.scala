package com.test.examples.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.cassandra.CassandraSQLContext
import org.apache.spark.SparkContext
import org.apache.spark.sql.cassandra.CassandraSQLContext._
import org.apache.spark.sql.SQLContext

object CassandraSQLContextExample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("Testing with CassandraSQLContext")

    conf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(conf)

    val cassandraSql = new CassandraSQLContext(sparkContext)

    val schemaRDD = cassandraSql.sql("select id,name,year  from shafi_cql.student")

    schemaRDD.collect().foreach { println }

    schemaRDD.registerTempTable("student")

    println("executing upto here")

    val studentRDD = cassandraSql.sql("select id,name,year from student where name == 'Shafiuddin SM' ")

    studentRDD.show();

    val sqlContext = new SQLContext(sparkContext)

  }
}