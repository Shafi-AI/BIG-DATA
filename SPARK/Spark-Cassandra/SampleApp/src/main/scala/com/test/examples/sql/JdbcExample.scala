package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import java.util.Properties

object JdbcExample {

  def main(args: Array[String]): Unit = {

    val sc: SparkContext = new SparkContext("local", "JDBC Example")

    val sqlContext: SQLContext = new SQLContext(sc)

    /*
    val jdbcDF = sqlContext.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:mydb", "username" -> "smshafiuddin", "password" -> "Johnny@1980", "dbtable" -> "mydb.emp")).jdbc(url, table, properties)
*/

    val url = "jdbc:mysql://localhost:3306/mydb"
    val props = new Properties();
    props.setProperty("user", "smshafiuddin")

    props.setProperty("password", "Johnny@1980")

    val jdbcDF = sqlContext.read.jdbc(url, "emp", props)

    jdbcDF.show()

    val mysqlDF = sqlContext.read.format("jdbc").option("url", "jdbc:mysql://localhost:3306/mydb")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "emp")
      .option("user", "smshafiuddin")
      .option("password", "Johnny@1980")
      .load()
    println("printing through select query ")

    mysqlDF.registerTempTable("emptable")
    mysqlDF.sqlContext.sql("select * from emptable").collect().foreach { println }

    mysqlDF.show()

     mysqlDF.sqlContext.sql("select * from emptable where name = 'shafiuddin'").collect().foreach { println }

    // jdbcDF.write.insertInto(tableName)
  }
}