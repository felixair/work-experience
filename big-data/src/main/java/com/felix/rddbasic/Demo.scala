package com.felix.rddbasic

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Author: Kefu Qin
  * Date: 2019-01-11 16:38
  * E-mail: kefu.qin@ericsson.com
  **/
object Demo {


  def main(args: Array[String]): Unit = {
    //    testConnHive()
    println(System.getProperty("user.dir"))
    //    wordCount()

    //    mapPart()
    calSum()
  }


  def calSum(): Unit ={
    val conf = new SparkConf().setAppName("GroupSort").setMaster("local")
    val spark = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
    val sc = spark.sparkContext
    val arr = Array(("key1", 4), ("key2", 5), ("key1", 2), ("key3", 4), ("key1", 6))
    val rdd = sc.parallelize(arr)

    val valSum = rdd.values.reduce(_+_)
    println(valSum)

    val eachSum = rdd.reduceByKey((x,y) => x + y)
    eachSum.collect().foreach(f=>println(f._1, f._2))

    val accum= sc.accumulator(0, "Accumulator")
    val rdd1 = rdd.keys
    rdd1.collect.foreach(f => println(f))
    val rdd2 = rdd.values
    rdd2.collect.foreach(f => println(f))
    //    val merge = rdd1.union(rdd2)
    //    merge.foreach(f => accum += 1)


    println(accum)
  }

  def mapPart(): Unit = {

    val conf = new SparkConf().setAppName("GroupSort").setMaster("local")
    val spark = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
    val sc = spark.sparkContext

    val numPart = sc.parallelize(1 to 9, 3)
    println("partition nums is : ", numPart.partitions.size)
    numPart.foreach(s => println(s.toString))

    numPart.mapPartitions(iterfunc).collect().foreach(f=>println(f._1, f._2))
  }

  def iterfunc[T] (iter: Iterator[T]) : Iterator[(T, T)] = {
    var res = List[(T, T)]()
    var pre = iter.next
    while(iter.hasNext) {
      val cur = iter.next
      res ::= (pre, cur)
      pre = cur
    }
    res.iterator
  }

  def wordCount(): Unit = {

    val path = "file:///" + System.getProperty("user.dir") + "/data4test/source.txt"

    val outpath="file:///" + System.getProperty("user.dir") + "/data4test/WC4result.txt"

    val conf = new SparkConf().setAppName("GroupSort").setMaster("local")
    val spark = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
    val sc = spark.sparkContext
    val testRdd = sc.textFile(path)
    val wordcount = testRdd.flatMap(_.split(" ")).map(x=>(x,1)).reduceByKey(_+_)

    val wordSort = wordcount.map(x=>(x._2, x._1)).sortByKey(false).map(x=>(x._2, x._1))
    wordSort.saveAsTextFile(outpath)
    sc.stop()
  }

  def testConnHive(): Unit = {

    val conf = new SparkConf()
    conf.setAppName("test")
      //      .setMaster("local")
      .setMaster("spark://100.98.97.128:7077")
      .setJars(List("C:\\felix\\project\\bigdata\\PCC\\git-space\\thailand-true-edos-nbi-solution\\SourceCode\\edos\\target\\edos.jar"))
    val spark = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    //    spark.sql("show databases").collect().foreach(println)

    spark.sql("use mpe1550465534212")

    val serviceDF = spark.sql("SELECT MEASURE_EARFCNDL FROM events_measure limit 10")
    serviceDF.collect().foreach(println)


  }

  def test(){
    val conf = new SparkConf().setAppName("GroupSort").setMaster("local")
    val spark = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()
    val sc = spark.sparkContext
    val test = List(("key1","123",12,2,0.13),("key1","123",12,3,0.18),("key2","234",12,1,0.09),("key1","345",12,8,0.75),("key2","456",12,5,0.45))
    val rdd = sc.parallelize(test)

    val rdd1= rdd.map(l => (l._1, (l._2, l._3, l._4, l._5))).groupByKey()
      .flatMap(line => {
        val topItem = line._2.toArray.sortBy(_._4)(Ordering[Double].reverse)
        topItem.map(f=>(line._1,f._1,f._4)).toList
      })
    rdd1.foreach(println)
    sc.stop()
  }

}