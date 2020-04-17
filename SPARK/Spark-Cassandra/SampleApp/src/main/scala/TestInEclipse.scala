

import org.apache.spark.SparkContext

object TestInEclipse {

  def main(args: Array[String]): Unit = {

    val txtFile = "/home/smshafiuddin/work/spark15eclipseconfig/SampleApp/src/main/scala/SampleApp.scala"
    val sc = new SparkContext("local", "Sample Application")
    val txtFileLines = sc.textFile(txtFile, 2).cache()
    val numAs = txtFileLines.filter(line => line.contains("val")).count()
    println("Lines with val: %s".format(numAs))

  }
}