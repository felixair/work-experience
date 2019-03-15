package com.felix.csvoper

import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.slf4j.LoggerFactory

/**
  * Author: Kefu Qin
  * Date: 2018-08-10 09:46
  * E-mail: kefu.qin@ericsson.com
  **/
object PgBizETL {

  val logger = LoggerFactory.getLogger(PgBizETL.getClass)

  case class SubService(serviceId: String, sbuscribeDay: String, subscriberDailyNum: String)
  case class PgData(transactionId: String, msisdn: String, operation: String, status: String, usrGrade: String, usrStatus:String, serviceCode: String, serviceUsageState: String, usrSessionPolicyCode:  String, time: String)

  def main(args: Array[String]): Unit = {
    PropertyConfigurator.configure("file/log4j.properties")
    val conf = new SparkConf().setAppName("PgBizETL").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val mysqlProps = new Properties()
    mysqlProps.put("driver", "com.mysql.jdbc.Driver")
    mysqlProps.put("user", "root")
    mysqlProps.put("password", "root123")

    val url = "jdbc:mysql://10.163.170.90:3306/pcc?useUnicode=true&characterEncoding=UTF-8"

    val lines = sc.textFile("output/pg")
    val fileRDD = lines.map(_.split("\\~", -1))
    val pgDF = fileRDD.filter(arr => "SUCCESSFUL".equals(arr(6))).map(arr => PgData(arr(3), arr(14), arr(5), arr(6), arr(18), arr(19), arr(22), arr(26), arr(27), arr(33))).toDF()

    // save the data to database table
//    pgDF.selectExpr("transactionId as transaction_id", "msisdn", "operation", "status", "usrGrade as usr_grade", "usrStatus as usr_status", "serviceCode as service_code", "serviceUsageState as service_usage_state", "usrSessionPolicyCode as usr_session_policy_code", "time").write.mode(SaveMode.Append).jdbc(url, "pg_data_result", mysqlProps)


    pgDF.registerTempTable("pgDF")

    // operation is subscribeService, get service_code
//    val pgSubServiceDF = sqlContext.sql("select serviceCode, substr(time,1,8) as time, count(*) as num from pgDF where operation = 'subscribeService' group by substr(time,1,8), serviceCode having trim(serviceCode) <> '' order by substr(time,1,8)")
//    pgSubServiceDF.selectExpr("serviceCode as service_id", "time as sbuscribe_day", "num as subscriber_daily_num").write.mode(SaveMode.Append).jdbc(url, "service_subscription", mysqlProps)

    // operation is subscribeUsrSessionPolicy, get usr_session_policy_code
    /*val pgSubUsrSessionPolicyDF = sqlContext.sql("select usrSessionPolicyCode, substr(time,1,8) as time, count(*) as num from pgDF where operation = 'subscribeUsrSessionPolicy' group by substr(time,1,8), usrSessionPolicyCode having trim(usrSessionPolicyCode) <> '' order by substr(time,1,8)")
    pgSubUsrSessionPolicyDF.selectExpr("usrSessionPolicyCode as service_id", "time as sbuscribe_day", "num as subscriber_daily_num").write.mode(SaveMode.Append).jdbc(url, "service_subscription", mysqlProps)
*/
    val pgSubUsrSessionPolicyDF = pgDF.filter($"operation" === "subscribeUsrSessionPolicy").groupBy("time", "usrSessionPolicyCode").agg("serviceCode" -> "count").orderBy("time")
    val newNames = Seq("day", "id", "num")
    pgSubUsrSessionPolicyDF.toDF(newNames: _*).selectExpr("id as service_id", "day as sbuscribe_day", "num as subscriber_daily_num").write.mode(SaveMode.Append).jdbc(url, "service_subscription", mysqlProps)
  }

}
