package com.felix.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * @program: work-experience
 * @description:
 * @author: Kefu Qin
 * @create: 2018-09-18 09:03
 **/
public class CheckHdfsFileExist {

	public static void main(String[] args) {

	}

	public static void checkHdfsExist() {

		try {
			//创建文件配置对象
			Configuration configuration = new Configuration();
			//定义URL
			URI uri = URI.create("hdfs://10.163.170.90:8020/");

			//获取文件系统
			FileSystem fileSystem = FileSystem.get(uri,configuration);
			Path path = new Path("/user/test/abc");
			boolean isFile = fileSystem.isFile(path);
			boolean isDirectory = fileSystem.isDirectory(path);
			System.out.println("isFile: " + isFile);
			System.out.println("isDirectory: " + isDirectory);

			fileSystem.create(new Path("/user/test/abc"));

//			fileSystem.open()

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
