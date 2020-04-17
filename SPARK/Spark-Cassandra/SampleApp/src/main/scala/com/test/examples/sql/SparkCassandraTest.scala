package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark._
import org.apache.spark.sql.cassandra.CassandraSQLContext

case class ShafiTable(id: Int, name: String, year: Int)

object SparkCassandraTest {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf(true)
    conf.set("spark.cassandra.connection.host", "localhost")
    val sc: SparkContext = new SparkContext("local", "SparkCassandra Test", conf)

    val data = sc.cassandraTable("shafi_cql", "student")

    data.collect().foreach(println)

    //  storing using somecolumns 

    val rdd = sc.parallelize(List((100, "SBI", 2003), (101, "SBH", 2002)))
    rdd.saveToCassandra("shafi_cql", "student", SomeColumns("id", "name", "year"))

    println(" saved normally")

    val storeData = sc.parallelize(List(ShafiTable(110, "Shafiuddin SM", 2003), ShafiTable(111, "Fareed", 2002)))

    storeData.saveToCassandra("shafi_cql", "student")

    println(" saved using case classes ")

    // sc.cassandraTable("shafi_cql", "student").collect().foreach { println }

    
    val allRows = sc.cassandraTable("shafi_cql", "student").map { row => ShafiTable(row.getInt(0), row.getString(1), row.getInt(2)) }

    println(" All Records ");
    
    allRows.collect().foreach { eachRecord => println(s"Id: " ,eachRecord.id, ", Name:", eachRecord.name, " Year:", eachRecord.year) }

    
  }

}