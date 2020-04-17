name := "Sample Project"
 
version := "1.0"
 
scalaVersion := "2.10.4"
 

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.2",
  "org.apache.spark" %% "spark-sql" % "1.5.2",
  "org.apache.spark" %% "spark-hive" % "1.5.2",
  "org.apache.spark" %% "spark-streaming" % "1.5.2",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.5.2",
  "org.apache.spark" %% "spark-streaming-flume" % "1.5.2",
  "org.apache.spark" %% "spark-mllib" % "1.5.2",
  "com.twitter" % "parquet-hadoop" % "1.6.0",
  "com.databricks" % "spark-csv_2.10" % "1.4.0",
  "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.5.2",
  "mysql" % "mysql-connector-java" % "5.1.37",
  "com.datastax.spark" % "spark-cassandra-connector_2.10" % "1.5.1",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.10.3"
  
  
  )

