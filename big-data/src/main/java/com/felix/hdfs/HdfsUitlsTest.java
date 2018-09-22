package com.felix.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;

/**
 * Author : GuangChen
 * Date : 2018-03-02 11:19
 * E-mail : guang.chen@ericsson.com
 */
public class HdfsUitlsTest {
    private FileSystem fs;

    @Before
    public void setUp() throws Exception {
        final URL coreSiteUrl = Class.forName(HdfsUitlsTest.class.getName()).getResource("/core-site.xml");
        final URL hdfsSiteUrl = Class.forName(HdfsUitlsTest.class.getName()).getResource("/hdfs-site.xml");
        final Configuration conf = new Configuration();
        conf.addResource(coreSiteUrl);
        conf.addResource(hdfsSiteUrl);
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser("ericsson");
        UserGroupInformation.setLoginUser(ugi);
        fs = ugi.doAs(new PrivilegedExceptionAction<FileSystem>() {
            public FileSystem run() throws Exception {
                return FileSystem.get(conf);
            }
        });
    }

    @After
    public void cleanUp(){
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void copyLocalFileToHDFS() {
        try {
            boolean result = HdfsUitls.copyLocalFileToHDFS(fs, "C:\\tttt\\job-scheduler-1.0.0.jar",
                    "/user/ericsson/pcc", true, true);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadHDFSFileToLocal() {
        try {
            boolean result = HdfsUitls.downloadHDFSFileToLocal(fs, "/user/ericsson/pcc/ebm-func.jar", "C:\\tttt\\");
            System.out.println(result);
//        FSDataInputStream fsdi = fs.open(new Path("/user/ericsson/pcc/ebm-func.jar"));
//        OutputStream output = new FileOutputStream("C:\\tttt\\adf.jar");
//        IOUtils.copyBytes(fsdi,output,4096,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void copyLocalFolderToHDFS() {
        try {
            boolean result = HdfsUitls.copyLocalFolderToHDFS(fs, "C:\\tttt\\", "/user/ericsson/pcc");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadHDFSFolderToLocal() throws IOException {
        boolean result = HdfsUitls.downloadHDFSFolderToLocal(fs, "/user/ericsson/pcc/", "C:\\tttt\\");
        System.out.println(result);
    }
}
