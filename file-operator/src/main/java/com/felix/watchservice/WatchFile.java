package com.felix.watchservice;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.*;
import java.util.Properties;

/**
 * Created by felix on 2019/3/24.
 */
public class WatchFile {

    private static WatchService watchService;
    private static Properties properties = new Properties();
    private static String filePath;

    public void watch() {

        // conf目录全路径
        filePath = (new File("conf").getAbsolutePath());

        try {

            // 获取文件系统的WatchService对象
            watchService = FileSystems.getDefault().newWatchService();
            // 监听'filePath'是否修改；register()方法后面监听事件种类还可以增加。
            Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置后台线程
        Thread watchThread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    try {

                        // 获取下一个文件改动事件
                        WatchKey key = watchService.take();

                        for (WatchEvent<?> event : key.pollEvents()) {

                            System.out.println(filePath + "/" + event.context() + " --> " + event.kind());
                            System.out.println("重新加载 ： " + filePath + "/" + event.context());
                            properties.load(new FileInputStream(new File(filePath + "/" + event.context())));

                        }

                        // 重设WatchKey
                        boolean valid = key.reset();

                        // 如果重设失败，退出监听
                        if (!valid) {
                            break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        // 设置为后台守护线程
        watchThread.setDaemon(true);
        watchThread.start();

    }

    /**
     * 模拟测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        properties.load(new FileInputStream(new File("sys.properties")));

        // 程序启动；配置类第一次加载配置文件(配置类为单例)。
        /*properties.load(new FileInputStream(new File("./conf/sysConfig2.properties")));
        System.out.println("username1 : " + properties.get("username"));
        System.out.println("age1 : " + properties.get("age"));

        // 开启监听程序，如有改动，则更新对象
        WatchFile w = new WatchFile();
        w.watch();

        // 假装做一些事情，延迟8秒。
        Thread.sleep(8000);

        // 延期之后，重新读取配置类中的参数。
        System.out.println("username2 : " + properties.get("username"));
        System.out.println("age2 : " + properties.get("age"));*/
    }

}
