package com.felix.file;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

public class UploadBigFile {

    public static void main(String[] args) throws Exception {

        String serverPath = "/opt/ericsson/ts/test-big.txt";
        String local_path = "C:\\felix\\personal\\project\\testfiles\\test-big.txt";

        uploadToSftp(serverPath, local_path);

    }

    public static boolean uploadToSftp(String serverPath, String local_path) {

        System.out.println("[uploadToSftp] start！");
        Long startTime = System.currentTimeMillis();

        ChannelSftp sftp = null;
        Channel channel = null;
        Session sshSession = null;

        try {
            JSch jsch = new JSch();
            jsch.getSession("ericsson", "100.98.97.200", 22);
            sshSession = jsch.getSession("ericsson", "100.98.97.200", 22);
            sshSession.setPassword("ericsson");
            Properties sshConfig = new Properties();
            // sshSession.setConfig("userauth.gssapi-with-mic", "no");
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);

            sshSession.setTimeout(5000);
            sshSession.connect();

            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;

            if (!sftp.isConnected()) {
                return false;
            }

            File file = new File(serverPath);
            if (!file.exists()) {
                file.mkdir();
            }
            String dst = serverPath;
            File file2 = new File(local_path);
            if (file2.length() == 0) {
                return false;
            }
            sftp.put(local_path, dst, ChannelSftp.OVERWRITE); // ChannelSftp.RESUME可实现断点续传
            sftp.quit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeChannel(sftp);
            closeChannel(channel);
            closeSession(sshSession);
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("total time is : " + (endTime - startTime));
        System.out.println("[uploadToSftp] end！");

        return true;
    }

    private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private static void millisToMin(long millis ) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        System.out.println((millis) + " = " + formatter.format(calendar.getTime()));
    }

    public static void uploadToFtp() throws Exception {
        InputStream inputStream = new FileInputStream(new File("C:\\felix\\personal\\project\\testfiles\\test.txt"));
        boolean flag = false;

        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        try {
            // 连接FTP服务器
            ftpClient.connect("100.98.97.200", 22);
            // 登录FTP服务器
            ftpClient.login("ericsson", "ericsson");
            // 是否成功登录FTP服务器
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                flag = false;
            }

            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.makeDirectory("/opt/ericsson/ts/testFile");
            ftpClient.changeWorkingDirectory("/opt/ericsson/ts/testFile");
            ftpClient.storeFile("/abc", inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
