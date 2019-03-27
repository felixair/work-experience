package com.felix.utils;

/**
 * Author : GuangChen
 * Date : 2018-03-01 09:52
 * E-mail : guang.chen@ericsson.com
 */
public class StringUtils {

    private static final String LINUX_FILE_SEPERATOR = "/";


    public static String trimHDFSPath(String path) {
        String result;
        if (path == null) {
            return "";
        }
        int position = path.lastIndexOf(LINUX_FILE_SEPERATOR);
        if (position == path.length() - 1) {
            result = path;
        } else {
            result = path + "/";
        }
        return result;
    }

    public static String trimLocalPath(String path) {
        String result;
        if (path == null) {
            return "";
        }
        String fileSeparator = System.getProperty("file.separator");
        int position = path.lastIndexOf(fileSeparator);
        if (position == path.length() - 1) {
            result = path;
        } else {
            result = path + fileSeparator;
        }
        return result;
    }

    public static String trimUnixPath(String path) {
        String result;
        if (path == null) {
            return "";
        }
        int position = path.lastIndexOf(LINUX_FILE_SEPERATOR);
        if (position == path.length() - 1) {
            result = path;
        } else {
            result = path + LINUX_FILE_SEPERATOR;
        }
        return result;
    }

    public static String getLastFolderPath(String path) {
        String result;
        if (path == null) {
            return "";
        }
        String fileSeparator = System.getProperty("file.separator");
        int position = path.lastIndexOf(fileSeparator);
        result = path.substring(position + 1, path.length());
        return result;
    }

    public static String getUnixLastFolderPath(String path) {
        String result;
        if (path == null) {
            return "";
        }
        int position = path.lastIndexOf(LINUX_FILE_SEPERATOR);
        result = path.substring(position + 1, path.length());
        return result;
    }

    public static boolean ifNull(String str) {
        return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
    }

    public static String trim(String str) {
        return str != null && str.length() != 0 ? str.trim() : "";
    }

}
