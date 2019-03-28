package com.felix.hdfs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: work-experience
 * @description:
 * @author: Kefu Qin
 * @create: 2018-09-18 09:04
 **/
public class ReadFileAndEdit {

	public static void main(String[] args) {

	}
	public static void editCsvFileData(String filePath) {

		String fileContent = "";
		List<String> list = new ArrayList<>();
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {

					int temp = (int)(Math.random()*10);
					System.out.println("random num is : " + temp);
					if ( temp == 3 ) {
						continue;
					}
					fileContent += line + "\r\n";
					list.add(line);
				}
				read.close();
			}

			System.out.println(fileContent);

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			BufferedWriter bw = new BufferedWriter(outputStreamWriter);

			if (list.size() != 0) {
				for (String string : list) {
					bw.write(string);
					bw.newLine();
					System.out.println(string);
				}
			} else if (list.size() == 0) {
				bw.write("");
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
