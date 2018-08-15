package com.felix.csvoper

import org.apache.log4j.PropertyConfigurator

object LoadPgToHdfs {

  def main(args: Array[String]): Unit = {
    PropertyConfigurator.configure("file/log4j.properties")
    val conf = new SparkConf().setAppName("LoadPgToHdfs").setMaster("local")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("dataset/pg")
    lines.saveAsTextFile("output/pg")
  }
}
