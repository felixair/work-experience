import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Test {

	static String filePath = "C:/local_data/upload/Policy_StatisticsTable_LN_s12343423423eq_20180808111151.csv";
	static String extra_csv_str = "";
	static String COMMA_FOR_STR = ",";
	static String path = "c:/local_data/upload/";
	static String fileName = "writer.csv";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		List<Map<String, String>> csvList = new ArrayList<Map<String, String>>();
		Map<String, String> csvMap = new HashMap<String, String>();
		List<Map<String, String>> csvAttachList = new ArrayList<Map<String, String>>();
		
		Map<String, String> propMap = new HashMap<String, String>();
		Map<String, String> propAttachMap = new HashMap<String, String>();
		
		getAddStr();
		
		csvList = getCsvFile();
		propMap = getPropMap();
		csvMap = getCsvMap();
		
		parseCsvAndPropAttach(csvList, propMap, propAttachMap);
		
		parsePropAndCsvAttach(propMap, csvAttachList, csvMap, extra_csv_str);
		
		propMap.putAll(propAttachMap);
		csvList.addAll(csvAttachList);
		
		savePropFile(propMap);
		saveCsvFile(csvList, fileName);
		
		
		
	}
	
	/**
	 * @param csvList
	 * @param fileName
	 * @throws Exception
	 * @desc 保存CSV文件
	 */
	public static void saveCsvFile(List<Map<String, String>> csvList, String fileName) throws Exception {
		try {
			File csvFolder = new File(path);
			if (!csvFolder.exists()) {
				csvFolder.mkdirs();
			}
			
			File csv = new File(path + fileName); // CSV文件
			
			if (!csv.exists()) {
				csv.createNewFile();
			}/* else {
				csv.delete();
				csv.createNewFile();
			}*/
			
			// 追记模式
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
			
			for (Map<String, String> map : csvList) {
				
				// 新增一行数据
				bw.write(map.get("code") + COMMA_FOR_STR + map.get("num") + COMMA_FOR_STR + map.get("others"));
				bw.newLine();
			}
			
			bw.close();
		} catch (FileNotFoundException e) {
			// 捕获File对象生成时的异常
			e.printStackTrace();
		} catch (IOException e) {
			// 捕获BufferedWriter对象关闭时的异常
			e.printStackTrace();
		}
	}
	
	/**
	 * @param propMap
	 * @param csvAttachList
	 * @param csvMap
	 * @param addStr
	 * @desc // 循环prop文件信息，与csv文件的配置对比 <br/>
			 // 记录prop中存在，csv中不存在的policy_code和num信息
	 */
	public static void parsePropAndCsvAttach(Map<String, String> propMap, List<Map<String, String>> csvAttachList, Map<String, String> csvMap, String addStr) {

		Iterator<Map.Entry<String, String>> entries = propMap.entrySet().iterator();
		Map<String, String> tempMap = new HashMap<String, String>();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			String propCode = entry.getKey();
			String propNum = entry.getValue();
			if (csvMap.get(propCode) == null) {
				tempMap = new HashMap<String, String>();
				tempMap.put("code", propCode);
				tempMap.put("num", propNum);
				tempMap.put("others", addStr);
				csvAttachList.add(tempMap);
			}
		}

	}
	
	/**
	 * @param csvList
	 * @param propMap
	 * @param propAttachMap
	 * @desc // 循环csv文件信息，与prop文件的配置对比，如果有相同的code，则将num值相加，并更新到num <br/>
			 // 记录csv中存在,prop中不存在的policy_code和num信息
	 */
	public static void parseCsvAndPropAttach(List<Map<String, String>> csvList, Map<String, String> propMap, Map<String, String> propAttachMap) {
		for (Map<String, String> map : csvList) {
			String csv_code = map.get("code").toString();
			int csv_num = Integer.parseInt(map.get("num"));
			if (propMap.get(csv_code) != null) {
				String totalNum = String.valueOf(csv_num + Integer.parseInt(propMap.get(csv_code)));
				map.put("num", totalNum);
				propMap.put(csv_code, totalNum);
			} else {
				propAttachMap.put(csv_code, String.valueOf(csv_num));
			}
		}
	}
	
	/**
	 * 获取csv新加行需要补全的字符串
	 */
	public static void getAddStr() {
        try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));//换成你的文件名
			String line = reader.readLine();
			String[] item = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
			
			int csvLength = item.length;
			for (int i = 0; i < csvLength - 2; i ++) {
				extra_csv_str += "0,";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * @desc 获取csv文件内容，并整合
	 */
	public static List<Map<String, String>> getCsvFile() {
		List<Map<String, String>> csvList = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));//换成你的文件名
//            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while((line=reader.readLine())!=null){
                String[] item = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if (item.length >= 2) {
                	System.out.println(item[0] + " --- " + item[1]);
                    map = new HashMap<String, String>();
                    map.put("code", formatPolicyCode(item[0]));
                    map.put("num", item[1]);
                    map.put("others", line.substring(line.indexOf(item[1]) + item[1].length() + 1));
                    csvList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return csvList;
	}
	
	
	
	/**
	 * @return Map<String, String>
	 * @desc 获取Csv中policy_code和num组成Map
	 */
	public static Map<String, String> getCsvMap() {
		
		Map<String, String> csvMap = new HashMap<String, String>();
		
		try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));//换成你的文件名
            String line = null;
            while((line=reader.readLine())!=null){
                String[] item = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                csvMap.put(formatPolicyCode(item[0]), item[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return csvMap;
	}
	
	/**
	 * @return Map<String, String>
	 * @desc 获取配置文件，将key/value封装成Map返回
	 */
	public static Map<String, String> getPropMap() {
		
		Map<String, String> propMap = new HashMap<String, String>();
		
		String fileSeparator = System.getProperty("file.separator");
		String configPath = System.getProperty("user.dir") + fileSeparator + "conf" + fileSeparator
				+ "param.properties";
		Properties prop = loadConfig(configPath);
		
		Iterator<String> it = prop.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key + "=" + prop.getProperty(key));
			propMap.put(key, prop.getProperty(key));
		}
		
		return propMap;
	}
	
	/**
	 * @param propMap
	 * @throws Exception
	 * @desc 保存属性到properties文件
	 */
	public static void savePropFile(Map<String, String> propMap) throws Exception {
		Properties prop = new Properties();
		String fileSeparator = System.getProperty("file.separator");
		String configPath = System.getProperty("user.dir") + fileSeparator + "conf" + fileSeparator
				+ "param.properties";
		
		FileOutputStream oFile = new FileOutputStream(configPath);// new FileOutputStream(configPath, true); true表示追加打开
		
		Iterator<Map.Entry<String, String>> entries = propMap.entrySet().iterator(); 
		while (entries.hasNext()) { 
		  Map.Entry<String, String> entry = entries.next(); 
		  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		  prop.setProperty(entry.getKey(), entry.getValue());
		}
		
		prop.store(oFile, "");
		oFile.close();
	}

	public static Properties loadConfig(String filePath) {
		Properties props = new Properties();
		InputStream in;
		try {
			in = new FileInputStream(filePath);
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return props;
	}

	/**
	 * @param policyCode
	 * @desc 格式化policyCode，如果超过32位，则取后32位
	 */
	public static String formatPolicyCode(String policyCode) {
		if (null == policyCode || "".equals(policyCode)) {
			return "";
		} else {
			int policyCodeLen = policyCode.length();
			return policyCodeLen > 32 ? policyCode.substring(policyCodeLen - 32, policyCodeLen) : policyCode;
		}
			
	}

}
