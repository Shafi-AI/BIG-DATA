package com.test.examples.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.Minutes

object KafkaExample {
  def main(args: Array[String]): Unit = {

    val Array(zkQuorum, group, topics, numThreads) = args

    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")

    val ssc = new StreamingContext(sparkConf, Seconds(2))

    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)

    val words = lines.flatMap { _.split(" ") }

    val wordCounts = words.map { x => (x, 1) }.reduceByKeyAndWindow(_ + _, _ - _, Minutes(3), Seconds(2), 2)

    wordCounts.print()

    ssc.start()

    ssc.awaitTermination()

  }
}