package com.felix.io;

public class StringPerformance {

    private static StringBuilder sbu = new StringBuilder("a");
    private static StringBuffer sbf = new StringBuffer("a");
    private static String str = new String("a");

    public static void main(String[] args) {

        System.out.println("======================");
        testString(1000);
        System.out.println("----------------------");
        testString(10000);
        System.out.println("----------------------");
        testString(100000);
        System.out.println("----------------------");
    }

    public static void testString(long count) {

        StringBuilder sbu = new StringBuilder("a");
        StringBuffer sbf = new StringBuffer("a");
        String str = new String("a");

        long startSbuTime = System.currentTimeMillis();

        for (long i = 0; i < count; i++) {
            sbf.append(i);
        }
        long endSbuTime = System.currentTimeMillis();
        System.out.println("StringBuffer运行时间：" + (endSbuTime - startSbuTime));

        startSbuTime = System.currentTimeMillis();

        for (long i = 0; i < count; i++) {
            sbu.append(i);
        }
        endSbuTime = System.currentTimeMillis();
        System.out.println("StringBuilder运行时间：" + (endSbuTime - startSbuTime));

        startSbuTime = System.currentTimeMillis();
        for (long i = 0; i < count; i++) {
            str = str + i;
        }
        endSbuTime = System.currentTimeMillis();
        System.out.println("String运行时间：" + (endSbuTime - startSbuTime));
    }

}
