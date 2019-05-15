package com.felix.shell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by felix on 2019/5/15.
 */
public class InvokeShell {

    private static Properties properties = new Properties();
    private static String[] serverList;

    public static void main(String[] args) throws Exception {

        initDNS();

        System.out.println(getServerInfo());

    }

    public static String executeShellWithParams() {
        String[] cmd = {"/bin/sh","-c","test.sh parm1 parm2"};

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getServerInfo() {

        String result = "";
        Process ps = null;
        String shpath = "/Users/felix/pro/workspace/intelj/gitspace/work-experience/file-operator/conf/test.sh";
        try {
            ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(ps!=null){
                ps.destroy();
            }
        }
        return result;
    }


    public static void initDNS() throws Exception {
        String url = Thread.currentThread().getContextClassLoader().getResource("server.properties").getFile();
        InputStream in = new FileInputStream(url);
//        InputStream in = InvokeShell.class.getResourceAsStream("/server.properties");
//        FileInputStream in = new FileInputStream("server.properties");
        properties.load(in);
        String dnsServer = properties.getProperty("dns.server");
        serverList = dnsServer.split(",");
        System.out.println(serverList);
        in.close();
    }

}
