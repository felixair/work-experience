package com.felix.csvoper

import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory

object LoadSmsToHdfs {

  val logger = LoggerFactory.getLogger(LoadSmsToHdfs.getClass)
  val DELIMITER_FOR_LIST = "~"
  val DELIMITER_FOR_STRUCT = ":"
  val DELIMITER_FOR_FILE = ","
  val SPLIT_DELIMITER_FOR_FILE = "\\,"

  def main(args: Array[String]): Unit = {
    PropertyConfigurator.configure("file/log4j.properties")
    val conf = new SparkConf().setAppName("LoadSmsToHdfs").setMaster("local")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("dataset/sms")
    val result = lines.map(line => composeSms(line))
    result.saveAsTextFile("output/sms")
  }

  def composeSms(line: String): String = {
    val cols = line.split(SPLIT_DELIMITER_FOR_FILE, -1)
    val valueArr = new Array[String](12)
    valueArr(0) = cols(0)
    valueArr(1) = cols(1)
    valueArr(2) = cols(2)
    valueArr(3) = cols(3)
    valueArr(4) = cols(4)
    valueArr(5) = cols(5)
    valueArr(6) = cols(6)
    valueArr(7) = cols(7)
    valueArr(8) = cols(8)
    valueArr(9) = cols(9) + cols(10) + cols(11) + cols(12)
    valueArr(10) = cols(13)
    valueArr(11) = cols(14)
    valueArr.mkString(DELIMITER_FOR_FILE).replaceAll("null", "")
  }

}
