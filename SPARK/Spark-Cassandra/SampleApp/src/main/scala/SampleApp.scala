import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object SampleApp {
  def main(args: Array[String]) {
    
     val txtFile = "/home/smshafiuddin//SampleApp.scala"
    val conf = new SparkConf().setAppName("Sample Application")
    val sc = new SparkContext(conf)
    val txtFileLines = sc.textFile(txtFile , 2).cache()
    val numAs = txtFileLines .filter(line => line.contains("val")).count()
    println("Lines with val: %s".format(numAs))
    
    
  }
}
