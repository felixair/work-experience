import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class TestDom {
	
	private static Logger LOGGER = Logger.getLogger(TestDom.class);

	static String hdfs_path = "/user/test/temp.txt";
	static String hdfs_connect_url = "hdfs://10.163.170.90:8020";
	
	public static void main(String[] args) throws Exception {

//		final String fileName = "C:\\felix\\project\\bigdata\\PCC\\doc\\task\\pgdata-collection-code\\doc\\A20180730.0915-0930-HLRSUBSjob_CDB86.xml";
		final String fileName = "C:\\felix\\project\\bigdata\\PCC\\doc\\task\\pgdata-collection-code\\doc\\test.xml";
		
		// 解析xml
		parseXml(fileName);
		
//		System.out.println("------带有命名空间--------");
//		parseXmlWithNamespace2(fileName);
		
		// 将解析后的数据存储到hdfs上
//		createFile2Hdfs();
		
		// 将文件从hdfs存储到hive
//		loadFile2Hive();
		/*final long timeInterval = 2000;	//24 * 60 * 60 * 1000;
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					parseXml(fileName);
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		};
		
		Thread thread = new Thread(runnable);
		thread.start();*/

	}
	
	/**
	 * @param argList
	 * @throws Exception
	 * @desc 将数据存储为hdfs文件
	 */
	public static void createFile2Hdfs(List<List> argList) throws Exception {
		
//		List<List> argList = new ArrayList<List>();
//		List<String> arg = new ArrayList<String>();
//		arg.add("001");
//		arg.add("aaa");
//		argList.add(arg);
//		arg = new ArrayList<String>();
//		arg.add("002");
//		arg.add("bbb");
//		argList.add(arg);
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(new URI(hdfs_connect_url), conf);
		Path dstPath = new Path(hdfs_path); // 目标路径
		// 打开一个输出流
		FSDataOutputStream outputStream = fs.create(dstPath);
		StringBuffer sb = new StringBuffer();
		for (List<String> args : argList) {
			for (String value : args) {
				sb.append(value).append("	");
			}
			sb.deleteCharAt(sb.length() - 4);// 去掉最后一个分隔符
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length() - 2);// 去掉最后一个换行符
		byte[] contents = sb.toString().getBytes();
		outputStream.write(contents);
		outputStream.close();
		fs.close();
		System.out.println("-------------文件创建成功-------------");

	}
	
	/**
	 * @throws Exception
	 * @desc 将hdfs数据文件存储到hive
	 */
	public static void loadFile2Hive() throws Exception {
		
		String driverName = "org.apache.hive.jdbc.HiveDriver";
		try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //replace "hive" here with the name of the user the queries should run as
        Connection con = DriverManager.getConnection("jdbc:hive2://10.163.170.90:10000/test", "", "");
        Statement stmt = con.createStatement();
        String table_user = "data_collection_q";
        String user_sql = "load data inpath '" + hdfs_path + "' into table " + table_user;
        System.out.println("Running: " + user_sql);
        stmt.execute(user_sql);
        stmt.close();
        System.out.println("-------------数据存储成功-------------");

	}
	
	/**
	 * @param fileName
	 * @return List<List>
	 * @desc 读取xml两个节点的值，根据两节点的属性r的值进行匹配
	 */
	public static List<List> parseXml(String fileName) {
        File inputXml = new File(fileName);
        SAXReader saxReader = new SAXReader();
        List<List> result = new ArrayList<List>();

        try {
            Document document = saxReader.read(inputXml);
            Element users = document.getRootElement();
            
            System.out.println(users.getName());
            
            Map<String, String> measTypeMap = new HashMap<String, String>();
            List<Element> measTypes = users.element("measData").element("measInfo").elements("measType");
            for (Element measType : measTypes) {
                System.out.println(measType.getText() + "--" + measType.attributeValue("p"));
                measTypeMap.put(measType.attributeValue("p"), measType.getText());
            }
            
            Map<String, String> rsMap = new HashMap<String, String>();
            List<Element> rs = users.element("measData").element("measInfo").element("measValue").elements("r");
            for (Element r : rs) {
                System.out.println(r.getText() + "--" + r.attributeValue("p"));
                rsMap.put(r.attributeValue("p"), r.getText());
            }
            
            Iterator iterator = measTypeMap.keySet().iterator();
            while(iterator.hasNext()) {
            	Object key = iterator.next();
            	Object measTypeValue = measTypeMap.get(key);
            	Object rsValue = rsMap.get(key);
            	List<String> list = new ArrayList<String>();
            	list.add(String.valueOf(rsValue));
            	list.add(String.valueOf(measTypeValue));
            	result.add(list);
            }
            
            System.out.println("-------------解析xml成功-------------");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return result;
    }
	
	/**
	 * @param fileName
	 * @throws DocumentException 
	 * @desc 处理带有命名空间的xml文档
	 */
	public static void parseXmlWithNamespace2(String fileName) throws DocumentException {
		SAXReader saxReader = new SAXReader();  
        File file = new File(fileName);  
        Document document = saxReader.read(file);
  
        Map map = new HashMap();  
        map.put("design","http://www.3gpp.org/ftp/specs/archive/32_series/32.435#measCollec");  
        XPath x = document.createXPath("//design:measType");  
        x.setNamespaceURIs(map);  
        List<Element> nodelist = x.selectNodes(document); 
        System.out.println(nodelist.size());
        
		System.out.println("size: " + nodelist.size());
		for (Element measType : nodelist) {
            System.out.println(measType.getText() + "--" + measType.attributeValue("p"));
        }
	}
	
	/**
	 * @param fileName
	 * @throws DocumentException 
	 * @desc 处理带有命名空间的xml文档
	 */
	public static void parseXmlWithNamespace(String fileName) throws DocumentException {
		SAXReader sax = new SAXReader();
		//声明一个map用于保存命名空间
		Map<String,String> uris = new HashMap<String, String>();
		//给命名空间取一个别名
		uris.put("a", "http://www.3gpp.org/ftp/specs/archive/32_series/32.435#measCollec");
		//设置命名空间后再读取xml文档
		sax.getDocumentFactory().setXPathNamespaceURIs(uris);
		Document dom = sax.read(fileName);
		//然后使用带有命名空间的前缀查询即可。
		List<Element> measTypes = dom.selectNodes("//a:measType");
		System.out.println("size: " + measTypes.size());
		for (Element measType : measTypes) {
            System.out.println(measType.getText() + "--" + measType.attributeValue("p"));
        }
		
		System.out.println("-----------------");
		
		List<Element> measTypes2 = dom.selectNodes("/measCollecFile/measData/measInfo/measType");
		for (Element measType : measTypes) {
            System.out.println(measType.getText() + "--" + measType.attributeValue("p"));
        }
	}
	
	public static void parseXmlTest(String fileName) {
        File inputXml = new File(fileName);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputXml);
            Element users = document.getRootElement();
            
            System.out.println(users.getName());
            
            System.out.println("------------原始写法（无视命名空间）-----------");

            List<Element> measTypes = users.element("measData").element("measInfo").elements("measType");

            for (Element measType : measTypes) {
                System.out.println(measType.getText() + "--" + measType.attributeValue("p"));
            }

            System.out.println("----------全层级xpath（无命名空间）-------------");

            List<Element> rs = document.selectNodes("/measCollecFile/measData/measInfo/measType");

            for (Element r : rs) {
                System.out.println(r.getText() + "--" + r.attributeValue("p"));
            }

            System.out.println("----------直接选择xpath（无命名空间）----------");
            
            List<Element> types = users.selectNodes("//measType");
            for (Element r : types) {
                System.out.println(r.getText() + "--" + r.attributeValue("p"));
            }
            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
