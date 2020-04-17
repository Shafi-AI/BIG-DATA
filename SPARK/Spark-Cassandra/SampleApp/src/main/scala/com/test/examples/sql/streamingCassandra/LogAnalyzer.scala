package com.test.examples.sql.streamingCassandra

import java.util.regex.Matcher
import java.util.regex.Pattern
import scala.util.Random

class LogAnalyzer extends Serializable {

  def createDaMap(m: Matcher): Map[String, String] = {

    return Map[String, String](
      ("id" -> scala.util.Random.nextInt(76080).toString),
      ("ip" -> m.group(1)),
      ("client" -> m.group(2)),
      ("user" -> m.group(3)),
      ("datetime" -> m.group(4)),
      ("request" -> m.group(5)),
      ("status" -> m.group(6)),
      ("bytes" -> m.group(7)),
      ("referer" -> m.group(8)),
      ("agent" -> m.group(9)))
  }

  def createSeq(m: Matcher): Seq[(Int, String, String, String, String, String, String, String, String, String)] = {

    Seq((Random.nextInt(76080), m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7), m.group(8), m.group(9)))

  }

  def createSeq3(m: Matcher): Seq[(Int, String, String, String)] = {

    Seq((Random.nextInt(76080), m.group(1), m.group(5), m.group(9)))

  }

  def transformLogData(logLine: String): Map[String, String] = {
    println(s"line:", logLine)
    val ddd = "\\d{1,3}"
    val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"
    val client = "(\\S+)"
    val user = "(\\S+)"
    val dateTime = "(\\[.+?\\])"
    val request = "\"(.*?)\""
    val status = "(\\d{3})"
    val bytes = "(\\S+)"
    val referer = "\"(.*?)\""
    val agent = "\"(.*?)\""

    val LOG_ENTRY_PATTERN = s"$ip $client $user $dateTime $request $status $bytes $referer $agent"
    val PATTERN = Pattern.compile(LOG_ENTRY_PATTERN)
    val matcher = PATTERN.matcher(logLine)

    if (matcher.matches()) {

      createDaMap(matcher)
    }

    null
  }

  def transformLogDataNew(logLine: String): Matcher = {

    println(s"line:", logLine)
    val ddd = "\\d{1,3}"
    val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"
    val client = "(\\S+)"
    val user = "(\\S+)"
    val dateTime = "(\\[.+?\\])"
    val request = "\"(.*?)\""
    val status = "(\\d{3})"
    val bytes = "(\\S+)"
    val referer = "\"(.*?)\""
    val agent = "\"(.*?)\""

    val LOG_ENTRY_PATTERN = s"$ip $client $user $dateTime $request $status $bytes $referer $agent"
    val PATTERN = Pattern.compile(LOG_ENTRY_PATTERN)
    val matcher = PATTERN.matcher(logLine)

    matcher
  }

  def transformDataIntoLogTable(logLine: String): Seq[(Int, String, String, String, String, String, String, String, String, String)] = {

    println(s"line:", logLine)
    val ddd = "\\d{1,3}"
    val ip = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"
    val client = "(\\S+)"
    val user = "(\\S+)"
    val dateTime = "(\\[.+?\\])"
    val request = "\"(.*?)\""
    val status = "(\\d{3})"
    val bytes = "(\\S+)"
    val referer = "\"(.*?)\""
    val agent = "\"(.*?)\""

    val LOG_ENTRY_PATTERN = s"$ip $client $user $dateTime $request $status $bytes $referer $agent"
    val PATTERN = Pattern.compile(LOG_ENTRY_PATTERN)
    val matcher = PATTERN.matcher(logLine)

    if (matcher.matches())
      createSeq(matcher)
    else
      Seq()
  }

}