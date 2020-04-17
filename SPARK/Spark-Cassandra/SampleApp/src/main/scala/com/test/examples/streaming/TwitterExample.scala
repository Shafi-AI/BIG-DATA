package com.test.examples.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import twitter4j.conf.ConfigurationBuilder
import twitter4j.auth.OAuthAuthorization
import org.apache.spark.streaming.twitter
import org.apache.spark.streaming.twitter.TwitterUtils

object TwitterExample {
 
  
  def main(args: Array[String]): Unit = {
    
    val appName =  "TwitterData"
    
    val conf = new SparkConf().setMaster("local[3]").setAppName(appName)
    
    val ssc  = new StreamingContext(conf,Seconds(5))
    
    val Array(consumerKey,consumerSecret,accessToken,accessTokenSecret) = args.take(4)
    
    val filters = args.takeRight(args.length - 4)
    val cb = new ConfigurationBuilder
    
 
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret)
    
    
    val auth = new OAuthAuthorization(cb.build())
    
    val tweets = TwitterUtils.createStream(ssc,Some(auth))
    
    val englishTweets = tweets.map { x => x.toString() }
    
    //val englishTweets = tweets.filter { _.getLang() == "en"}
    
    
    
    englishTweets.saveAsTextFiles("tweets", "json")
    
    ssc.start()
    ssc.awaitTermination()
    
    
    
    
    
    
  }
}