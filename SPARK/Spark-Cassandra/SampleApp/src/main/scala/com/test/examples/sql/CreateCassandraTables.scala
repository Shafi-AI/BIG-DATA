package com.test.examples.sql

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import com.datastax.spark.connector._
import org.apache.spark._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import com.datastax.spark.connector.cql.CassandraConnector
import scala.util.Random
import org.apache.spark.rdd.RDD

object CreateCassandraTables {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("Testing with CassandraSQLContext")

    conf.set("spark.cassandra.connection.host", "localhost")

    val sparkContext = new SparkContext(conf)

    // val sqlContext = new SQLContext(sparkContext)

    CassandraConnector(conf).withSessionDo { session =>

      session.execute("CREATE KEYSPACE IF NOT EXISTS SHAFI_ECL_CREATE WITH REPLICATION ={'class':'SimpleStrategy','replication_factor': 1}")

      session.execute("DROP TABLE IF EXISTS SHAFI_ECL_CREATE.SQL_DEMO")
      session.execute("CREATE TABLE SHAFI_ECL_CREATE.SQL_DEMO (ID INT PRIMARY KEY, NAME TEXT, YEAR INT)")
      session.execute("INSERT INTO SHAFI_ECL_CREATE.SQL_DEMO (ID,NAME,YEAR) VALUES (1,'SHAFIUDDIN SM',2010)")

      session.execute("CREATE KEYSPACE IF NOT EXISTS SHAFI_JOIN_TEST WITH REPLICATION ={'class':'SimpleStrategy','replication_factor': 1}")
      session.execute("DROP TABLE IF EXISTS SHAFI_JOIN_TEST.USER_VISITS")
      session.execute("CREATE TABLE SHAFI_JOIN_TEST.USER_VISITS (USER TEXT, UTC_MILLIS BIGINT, STORE TEXT,  PRIMARY KEY(USER,UTC_MILLIS)) WITH CLUSTERING ORDER BY (UTC_MILLIS DESC)")

      session.execute("DROP TABLE IF EXISTS SHAFI_JOIN_TEST.STORES")
      session.execute("CREATE TABLE SHAFI_JOIN_TEST.STORES (CITY TEXT, STORE TEXT, MANAGER TEXT,  PRIMARY KEY(CITY,STORE))")

    }

    /*
    val rdd = sparkContext.cassandraTable("shafi_ecl_create", "sql_demo")

    rdd.collect().foreach { println } */

    val numStores = 128

    randomStores(sparkContext, numStores, cities = 32).
      saveToCassandra("shafi_join_test", "stores", SomeColumns("city", "store", "manager"))

    randomVisists(sparkContext, users = 16384, visitsPerUser = 16, stores = numStores)
      .saveToCassandra("shafi_join_test", "user_visits", SomeColumns("user", "utc_millis", "store"))

    /* val stores = sparkContext.cassandraTable("shafi_join_test", "stores")
    println(s"Total No of stores: " + stores.collect().length)

    val userAndVisits = sparkContext.cassandraTable("shafi_join_test", "user_visits")
    println(s"Total No of User Per Visits: " + userAndVisits.collect.length)
*/
    sparkContext.stop

  }

  def randomStores(sc: SparkContext, stores: Int, cities: Int): RDD[(String, String, String)] = {

    sc.parallelize(0 until stores).map { s =>

      val city = s"city_${Random.nextInt(cities)}"

      val store = s"store_${s}";

      val manager = s"manager_${Math.abs(Random.nextInt())}"

      (city, store, manager)
    }
  }

  def randomVisists(sc: SparkContext, users: Int, stores: Int, visitsPerUser: Int): RDD[(String, Long, String)] = {

    sc.parallelize(0 until users)
      .flatMap { u =>
        val user = s"user_${u}"

        (1 to visitsPerUser).map { v =>
          val utcmillis = System.currentTimeMillis() - Random.nextInt

          val store = s"store_${Random.nextInt(stores)}"

          (user, utcmillis, store)
        }

      }

  }

}