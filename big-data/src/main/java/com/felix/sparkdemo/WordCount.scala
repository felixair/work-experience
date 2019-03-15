package com.felix.sparkdemo

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Author: Kefu Qin
  * Date: 2019-01-18 09:26
  * E-mail: kefu.qin@ericsson.com
  **/
object WordCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    val sc = new SparkContext(conf)

    val path = "C:\\felix\\personal\\project\\testfiles\\wordcount.txt"
    val lines = sc.textFile(path)
    val rdd = lines.flatMap(line => line.split(" ")).map(word => (word,1)).reduceByKey(_+_)

    rdd.collect()
    rdd.foreach(println)

    sc.stop()


  }

}
