package com.test.examples.sql.cassandraTables.hadoop

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.mapreduce.OutputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.mapred.JobConf


object ReadWriteHDFS {
  def main(args: Array[String]): Unit = {
    
    val sparkConf  = new SparkConf().setMaster("local").setAppName("ReadWriteHDFS")
    
    val sparkContext  = new SparkContext(sparkConf)
    
    val textFile = sparkContext.textFile("hdfs://localhost:8020/home/smshafiuddin/work/README.md")
    
    val wordSplit  = textFile.flatMap { line => line.split(" ") }
    
    val  wordPairs  = wordSplit.map { word => (word,1) }
    
    val wordCounts   =  wordPairs.reduceByKey(_+_)
    
    wordCounts.take(5).foreach(println)
    
    // wordCounts.saveAsTextFile("hdfs://localhost:8020/home/smshafiuddin/work/wordcountresultfromspark") -- // This stores as text file 
    
    // wordCounts.saveAsObjectFile("hdfs://localhost:8020/home/smshafiuddin/work/wordcountresultfromSaveAsObjectFile") -- This stores as sequence file // -- Done
    
    // wordCounts.saveAsHadoopFile("/home/smshafiuddin/work/saveHadoopFileExample", classOf[Text], classOf[Text], classOf[TextOutputFormat[Text,Text]]) // -- Done
   
    // wordCounts.saveAsNewAPIHadoopFile("hdfs://localhost:8020/home/smshafiuddin/work/saveAsNewAPIHadoopFile", classOf[Text],classOf[Text],classOf[TextOutputFormat[Text,Text]])  // -- Done
    
    
    
    /* for saveAsNewAPIHadoopDataset
     * val conf  =  new Configuration()
    conf.set("mapreduce.job.output.key.class",classOf[Text].getName)
    conf.set("mapreduce.job.output.value.class",classOf[LongWritable].getName)
    conf.set("mapreduce.outputformat.class",classOf[TextOutputFormat[Text,Text]].getName)
    conf.set("mapreduce.output.fileoutputformat.outputdir","hdfs://localhost:8020/home/smshafiuddin/work/saveAsNewAPIHadoopDataset")
    
    
    
    
    wordCounts.saveAsNewAPIHadoopDataset(conf)
 */    
    
    /*  for saveAsHadoopDataset
    val conf  =  new Configuration()
    conf.set("mapred.job.output.key.class",classOf[Text].getName)
    conf.set("mapred.job.output.value.class",classOf[LongWritable].getName)
    conf.set("mapred.outputformat.class",classOf[TextOutputFormat[Text,Text]].getName)
    conf.set("mapreduce.output.fileoutputformat.outputdir","hdfs://localhost:8020/home/smshafiuddin/work/saveAsHadoopDataset")
    val jobConf  = new JobConf(conf)
    
    wordCounts.saveAsHadoopDataset(jobConf)  // --  Done 
    * 
    * */
    
    
  }
}