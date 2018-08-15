package com.felix.csvoper

import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory

/**
  * Author : Kefu Qin
  * Date : 2018-07-30 15:33
  * E-mail : kefu.qin@ericsson.com
  */
object SmsBizETL {

  val logger = LoggerFactory.getLogger(SmsBizETL.getClass)

  case class SubNty(policyId: String, msisdn: String, nodeName: String, time: String, form: String, result:String)


  def main(args: Array[String]): Unit = {
    PropertyConfigurator.configure("file/log4j.properties")
    val conf = new SparkConf().setAppName("SmsBizETL").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val mysqlProps = new Properties()
    mysqlProps.put("driver", "com.mysql.jdbc.Driver")
    mysqlProps.put("user", "root")
    mysqlProps.put("password", "root123")

    val url = "jdbc:mysql://10.163.170.90:3306/pcc?useUnicode=true&characterEncoding=UTF-8"
    val table = "config"
    val configDF = sqlContext.read.jdbc(url, table, mysqlProps).cache()

    val lines = sc.textFile("output/sms")
    val fileRDD = lines.map(_.split("\\,", -1))
    val qosDF = fileRDD.map(arr => SubNty(arr(0), arr(1), arr(11), arr(9), arr(0), arr(0))).toDF()
    val joinResDF = qosDF.join(configDF, qosDF("policyId") === configDF("sms_key"))
    joinResDF.selectExpr("msisdn as subscriber", "time as notify_time", "policyId as policy_id", "form", "result").write.mode(SaveMode.Append).jdbc(url, "subscriber_notification", mysqlProps)
    joinResDF.selectExpr("nodeName as node_name", "epc_rule_id", "policy_code", " '' as subscriber_group_id"," '' as subscribed_service", "msisdn","time").write.mode(SaveMode.Append).save("sms_data_result")


  }

}
