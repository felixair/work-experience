package com.felix.utils;

public class StringTransferUtils {

    private static String[] binaryArray =
            {"0000","0001","0010","0011",
            "0100","0101","0110","0111",
            "1000","1001","1010","1011",
            "1100","1101","1110","1111"};
    private static String hexStr =  "0123456789ABCDEF";

    /**
     * @return 二进制数组转换为二进制字符串   2->2
     */
    public static String bytes2BinStr(byte[] bArray){
        String outStr = "";
        int pos = 0;
        for(byte b:bArray){
            //高四位
            pos = (b&0xF0)>>4;
            outStr+=binaryArray[pos];
            //低四位
            pos=b&0x0F;
            outStr+=binaryArray[pos];
        }
        return outStr;
    }
    /**
     * @return 将十六进制转换为二进制字节数组   16->2
     */
    public static byte[] hexStr2BinArr(String hexString){
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length()/2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for(int i=0;i<len;i++){
            //右移四位得到高位
            high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
            bytes[i] = (byte) (high|low);//高地位做或运算
        }
        return bytes;
    }

    public static String hexStr2BinStr(String hexString){
        return bytes2BinStr(hexStr2BinArr(hexString));
    }



    public static void main(String[] args) {
        String str = hexStr2BinStr("0025F040800198");
        System.out.println("binary GUMMEI is : " + str);
        // MMEI = MMEGroupId (MME Group ID, 16bits) + MMECode (MME Code, 8bits)
        String MMEI = str.substring(str.length() - 24);

        System.out.println("binary MMEI is : " + MMEI);

        String MMECode = MMEI.substring(MMEI.length() - 8);
        String MMEGroupId = MMEI.substring(0, MMEI.length() - 8);

        System.out.println("binary MMECode is : " + MMECode);
        System.out.println("binary MMEGroupId is : " + MMEGroupId);

        System.out.println("decimal MMECode is : " + Integer.parseInt(MMECode, 2));
        System.out.println("decimal MMEGroupId is : " + Integer.parseInt(MMEGroupId, 2));


    }

}
