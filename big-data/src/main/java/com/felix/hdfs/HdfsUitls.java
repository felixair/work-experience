package com.felix.hdfs;

import com.felix.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Author : GuangChen
 * Date : 2018-03-01 11:15
 * E-mail : guang.chen@ericsson.com
 * <p>
 * 1.upload single file to hdfs
 * 2.upload folder's files to hdfs
 * 3.download single hdfs file to local
 * 4.download hdfs folder's files to local
 * <p>
 * <p>
 * I am thinking whether using hadoop filesystem api FileSystem.getLocal(conf).getRawFileSystem() to get the
 * LocalFilesystem or not.
 * <p>
 * <br>
 * To Be continue...
 * </br>
 */
public class HdfsUitls {
    private static Logger logger = Logger.getLogger(HdfsUitls.class);

    private final static String WRITING_FLAG = ".tmp";

    /**
     * This method is used to upload local File to Hadoop file system
     *
     * @param fs             Hadoop FileSystem
     * @param localFilePath  e.g.  linux: "/opt/ericsson/pcc/bigdata/xxx.txt" windows: "C:\\xxx\\xxx\\xxx.csv"
     * @param hdfsFolderPath e.g. "/user/ericsson/pcc"
     * @param deleteSource   if delete source file after uploaded to hdfs, true:delete, false: keep file
     * @param overwrite      if a file has the same hdfs path exists, whether or not overwrite it
     * @return uploaded file successfully or not
     */
    public static boolean copyLocalFileToHDFS(FileSystem fs, String localFilePath, String hdfsFolderPath, boolean
            deleteSource, boolean overwrite) throws IOException {
        boolean result = false;
        File localFile = new File(localFilePath);
        if (!localFile.isFile()) {
            throw new IOException("File " + localFilePath + " does not exist.");
        }
        Path destHDFSFolder = new Path(hdfsFolderPath);
        String hFolderPath = StringUtils.trimHDFSPath(hdfsFolderPath);
        String tmpHdfsFilePath = hFolderPath + localFile.getName() + WRITING_FLAG;
        Path tmpHdfsFile = new Path(tmpHdfsFilePath);
        String destHdfsFilePath = hFolderPath + localFile.getName();
        Path destHdfsFile = new Path(destHdfsFilePath);
        //if dest hdfs folder does not exist then create folder
        if (!fs.exists(destHDFSFolder)) {
            fs.mkdirs(destHDFSFolder);
        }

        if (fs.exists(destHdfsFile)) {
            //dest file exist
            if (overwrite) {
                //overwrite dest file
                fs.delete(destHdfsFile, false);
                fs.copyFromLocalFile(deleteSource, new Path(localFilePath), tmpHdfsFile);
                result = fs.rename(tmpHdfsFile, destHdfsFile);
            } else {
                throw new IOException("HDFS File " + destHdfsFilePath + " already exist.");
            }
        } else {
            //dest file does not exist, upload file then rename it
            fs.copyFromLocalFile(deleteSource, new Path(localFilePath), tmpHdfsFile);
            result = fs.rename(tmpHdfsFile, destHdfsFile);
        }
        return result;
    }

    /**
     * overwrite dest hdfs file if exist and keep local file
     *
     * @param fs
     * @param localFilePath  e.g. linux: "/opt/ericsson/pcc/bigdata/xxx.txt" windows: "C:\\xxx\\xxx\\xxx.csv"
     * @param hdfsFolderPath e.g. "/user/ericsson/pcc"
     * @return uploaded file successfully or not
     */
    public static boolean copyLocalFileToHDFS(FileSystem fs, String localFilePath, String hdfsFolderPath) throws
            IOException {
        return copyLocalFileToHDFS(fs, localFilePath, hdfsFolderPath, false, true);
    }


    /**
     * This method is used to download file from hdfs to local file system
     *
     * @param fs
     * @param hdfsFilePath    e.g. "/user/ericsson/pcc/xxx.csv"
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @param deleteSource    if delete hdfs source file after downloaded, true:delete, false: keep file
     * @param overwrite       if overwrite existing local file
     * @return downloaded file successfully or not
     */
    public static boolean downloadHDFSFileToLocal(FileSystem fs, String hdfsFilePath, String localFolderPath, boolean
            deleteSource, boolean overwrite) throws IOException {
        boolean result = false;
        Path srcFile = new Path(hdfsFilePath);
        if (!fs.isFile(srcFile)) {
            throw new IOException("HDFS File " + hdfsFilePath + " does not exist.");
        }
        File localFolder = new File(localFolderPath);
        if (!localFolder.exists()) {
            localFolder.mkdirs();
        }
        localFolderPath = StringUtils.trimLocalPath(localFolderPath);
        String fileName = srcFile.getName();
        String tmpLocalFilePath = localFolderPath + fileName + WRITING_FLAG;
        Path tmpLocalFile = new Path(tmpLocalFilePath);
        String localFilePath = localFolderPath + fileName;
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            if (overwrite) {
                localFile.deleteOnExit();
                fs.copyToLocalFile(deleteSource, srcFile, tmpLocalFile, true);
                //fs.copyToLocalFile(deleteSource, srcFile, tmpLocalFile);
                result = new File(tmpLocalFilePath).renameTo(localFile);
            } else {
                throw new IOException("Local File" + localFilePath + " already exist.");
            }
        } else {
            fs.copyToLocalFile(deleteSource, srcFile, tmpLocalFile, true);
            //fs.copyToLocalFile(deleteSource, srcFile, tmpLocalFile);
            result = new File(tmpLocalFilePath).renameTo(localFile);
        }
        return result;
    }

    /**
     * download file from hdfs to local file system
     * overwrite local file and keep source hdfs file
     *
     * @param fs
     * @param hdfsFilePath    e.g. "/user/ericsson/pcc/xxx.csv"
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @return downloaded file successfully or not
     */
    public static boolean downloadHDFSFileToLocal(FileSystem fs, String hdfsFilePath, String localFolderPath) throws
            IOException {
        return downloadHDFSFileToLocal(fs, hdfsFilePath, localFolderPath, false, true);
    }

    /**
     * upload local files to hdfs
     *
     * @param fs              Hadoop FileSystem
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @param hdfsFolderPath  e.g. "/user/ericsson/pcc"
     * @param deleteSource    if delete source file after uploaded to hdfs, true:delete, false: keep file
     * @param overwrite       if a file has the same hdfs path exists, whether or not overwrite it
     * @return uploaded files successfully or not
     */
    public static boolean copyLocalFolderToHDFS(FileSystem fs, String localFolderPath, String hdfsFolderPath, boolean
            deleteSource, boolean overwrite) throws IOException {
        boolean result = false;
        Path destHDFSPath = new Path(hdfsFolderPath);
        //check dest path first
        if (!fs.exists(destHDFSPath)) {
            throw new IOException("Destination HDFS folder [" + hdfsFolderPath + "] does not exists! ");
        } else if (fs.isFile(destHDFSPath)) {
            throw new IOException("Destination HDFS PATH [" + hdfsFolderPath + "] is not a directory! ");
        }
        File localFolder = new File(localFolderPath);
        if (!localFolder.exists()) {
            throw new IOException("Local folder [" + localFolderPath + "] does not exists! ");
        } else if (localFolder.isFile()) {
            throw new IOException("Local folder PATH [" + localFolderPath + "] is not a directory! ");
        }
        localFolderPath = StringUtils.trimLocalPath(localFolderPath);
        hdfsFolderPath = StringUtils.trimHDFSPath(hdfsFolderPath);

        File[] fileArray = localFolder.listFiles();
        if (fileArray == null || fileArray.length == 0) {
            logger.info("Not copy Empty Folder [" + localFolderPath + "]!");
            return true;
        } else {
            for (File file : fileArray) {
                if (!file.isDirectory()) {
                    result = copyLocalFileToHDFS(fs, file.getPath(), hdfsFolderPath, deleteSource, overwrite);
                } else {
                    String tmp = StringUtils.getLastFolderPath(file.getPath());
                    String childHdfsFolderPath = hdfsFolderPath + tmp;
                    Path childHdfsFolder = new Path(childHdfsFolderPath);
                    if (!fs.exists(childHdfsFolder)) {
                        fs.mkdirs(new Path(childHdfsFolderPath));
                    }
                    result = copyLocalFolderToHDFS(fs, file.getPath(), childHdfsFolderPath, deleteSource,
                            overwrite);
                }
            }
        }
        if (deleteSource) {
            FileUtils.deleteDirectory(localFolder);
        }
        return result;
    }

    /**
     * upload local files to hdfs
     *
     * @param fs              Hadoop FileSystem
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @param hdfsFolderPath  e.g. "/user/ericsson/pcc"
     * @return uploaded files successfully or not
     */
    public static boolean copyLocalFolderToHDFS(FileSystem fs, String localFolderPath, String hdfsFolderPath) throws
            IOException {
        return copyLocalFolderToHDFS(fs, localFolderPath, hdfsFolderPath, false, true);
    }

    /**
     * download files by hdfs folder name to local disk
     *
     * @param fs              Hadoop FileSystem
     * @param hdfsFolderPath  e.g. "/user/ericsson/pcc"
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @param deleteSource    if delete hdfs source folder after downloaded, true:delete, false: keep file
     * @param overwrite       if overwrite existing local files
     * @return downloaded files successfully or not
     * @throws IOException
     */
    public static boolean downloadHDFSFolderToLocal(FileSystem fs, String hdfsFolderPath, String localFolderPath,
                                                    boolean deleteSource, boolean overwrite) throws IOException {
        boolean result = false;
        localFolderPath = StringUtils.trimLocalPath(localFolderPath);
        hdfsFolderPath = StringUtils.trimHDFSPath(hdfsFolderPath);

        Path hdfsFolder = new Path(hdfsFolderPath);
        if (!fs.exists(hdfsFolder)) {
            throw new IOException("Source HDFS folder [" + hdfsFolderPath + "] does not exists! ");
        } else if (!fs.isDirectory(hdfsFolder)) {
            throw new IOException("Source HDFS folder [" + hdfsFolderPath + "] is not a directory! ");
        }

        File localFolder = new File(localFolderPath);
        if (!localFolder.exists()) {
            throw new IOException("Local folder [" + localFolderPath + "] does not exists! ");
        } else if (!localFolder.isDirectory()) {
            throw new IOException("Local folder [" + localFolderPath + "] is not a directory! ");
        }
        FileStatus[] files = fs.listStatus(hdfsFolder);
        if (files == null || files.length == 0) {
            logger.info("Not copy Empty HDFS Folder [" + hdfsFolderPath + "]!");
            return true;
        } else {
            for (FileStatus status : files) {
                if (status.isDirectory()) {
                    String childFolderName = status.getPath().getName();
                    String childFolderPath = hdfsFolderPath + childFolderName;
                    String childLocalFolderPath = localFolderPath + childFolderName;
                    File childLocalFolder = new File(childLocalFolderPath);
                    if (!childLocalFolder.exists()) {
                        childLocalFolder.mkdirs();// permission problem
                    }
                    result = downloadHDFSFolderToLocal(fs, childFolderPath, childLocalFolderPath, deleteSource,
                            overwrite);
                } else if (status.isFile()) {
                    result = downloadHDFSFileToLocal(fs, status.getPath().toString(), localFolderPath, deleteSource,
                            overwrite);
                }
            }
        }
        if (deleteSource) {
            fs.delete(hdfsFolder, true);
        }
        return result;

    }

    /**
     * download files by hdfs folder name to local disk
     *
     * @param fs              Hadoop FileSystem
     * @param hdfsFolderPath  e.g. "/user/ericsson/pcc"
     * @param localFolderPath e.g.  linux: "/opt/ericsson/pcc/bigdata" windows: "C:\\xxx\\xxx"
     * @return downloaded files successfully or not
     * @throws IOException
     */
    public static boolean downloadHDFSFolderToLocal(FileSystem fs, String hdfsFolderPath, String localFolderPath)
            throws IOException {
        return downloadHDFSFolderToLocal(fs, hdfsFolderPath, localFolderPath, false, true);
    }

    public static void test() {
        Configuration conf = new Configuration();
        try {
            FileSystem fs = FileSystem.getLocal(conf).getRawFileSystem();
            Path path = new Path("C:\\tttt");
            System.out.println(fs.exists(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void iteratorShowFiles(FileSystem hdfs, Path path) {
        try {
            if (hdfs == null || path == null) {
                return;
            }
            FileStatus[] files = hdfs.listStatus(path);

            for (int i = 0; i < files.length; i++) {
                try {
                    if (files[i].isDirectory()) {
                        System.out.println(">>>" + files[i].getPath() + ", dir owner:" + files[i].getOwner());
                        iteratorShowFiles(hdfs, files[i].getPath());
                    } else if (files[i].isFile()) {
                        System.out.println("   " + files[i].getPath() + ", length:" + files[i].getLen()
                                + ", owner:" + files[i].getOwner());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
