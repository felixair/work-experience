package com.felix.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;

public class ReadBigFile {

    private static String srcFileName = "C:\\felix\\personal\\project\\testfiles\\test-big.txt";
    private static String targetFileName = "C:\\felix\\personal\\project\\testfiles\\test-copy.txt";

    public static void main(String[] args) throws Exception{
        System.out.println("======================================");
        fileCopySimple();
        System.out.println("--------------------------------------");
        fileCopy();
        System.out.println("--------------------------------------");
        readByBufferedReader();
        System.out.println("--------------------------------------");
        readByBufferReader();
        System.out.println("======================================");

    }

    BufferedReader b;
    InputStream c;


    public static void fileCopySimple() throws Exception {
        System.out.println("[fileCopySimple] start！");
        Long startTime = System.currentTimeMillis();

        int byteread = 0;
        File oldfile = new File(srcFileName);
        if (oldfile.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(srcFileName); //读入原文件
            FileOutputStream fs = new FileOutputStream(targetFileName);
            byte[] buffer = new byte[1444];
            while ( (byteread = inStream.read(buffer)) != -1) {
//                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }


        Long endTime = System.currentTimeMillis();
        System.out.println("total time is : " + (endTime - startTime));
        System.out.println("[fileCopySimple] end！");

    }

    public static void fileCopy() throws Exception{
        System.out.println("[fileCopy] start！");
        Long startTime = System.currentTimeMillis();
        RandomAccessFile file = new RandomAccessFile(srcFileName, "rw");

        file.seek(0);
        RandomAccessFile fileCopy = new RandomAccessFile("C:\\felix\\personal\\project\\testfiles\\test-copy2.txt", "rw");

        int len = (int) file.length();//取得文件长度（字节数）
        byte[] b = new byte[len];

        file.readFully(b);
        fileCopy.write(b);


        Long endTime = System.currentTimeMillis();
        System.out.println("total time is : " + (endTime - startTime));
        System.out.println("[fileCopy] end！");
    }


    public static void readByBufferedReader() throws IOException {
        System.out.println("[readByRandomFile] start！");
        Long startTime = System.currentTimeMillis();

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(srcFileName)));
        BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);// 10M缓存
        FileWriter fw = new FileWriter("C:\\felix\\personal\\project\\testfiles\\test-copy3.txt");
        while (in.ready()) {
            String line = in.readLine();
//            fw.write(line);
        }
        in.close();
        fw.flush();
        fw.close();

        Long endTime = System.currentTimeMillis();
        System.out.println("[readByRandomFile] : total time is : " + (endTime - startTime));
        System.out.println("[readByRandomFile] end！");
    }

    public static void readByBufferReader() {
        System.out.println("[readByBufferReader] start！");
        Long start = System.currentTimeMillis();
        File file = new File(srcFileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10*1024*1024);

            FileWriter out = new FileWriter("C:\\felix\\personal\\project\\testfiles\\test-copy4.txt");

            String line = "";
            while((line = in.readLine()) != null) {
//                out.write(line);
            }

            in.close();
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long end = System.currentTimeMillis();
        System.out.println("total time is : " + (end - start));
        System.out.println("[readByBufferReader] end！");
    }

}
