package com.felix.watchservice;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by felix on 2019/3/24.
 */
public class WatchServiceDemo {


    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        String projectPath = System.getProperty("user.dir");

        Path curPath = Paths.get(projectPath);
        WatchService watchService = curPath.getFileSystem().newWatchService();
        walkAndRegisterDirectories(curPath, watchService);
        try {
            //监听目录变化
            while (true) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent event : watchKey.pollEvents()) {
                    System.out.println(event.kind());
                    System.out.println("Operator type : " + event.kind());
                    System.out.println("File name : " + event.context().toString());
                    System.out.println("---------------");
                }
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void walkAndRegisterDirectories(Path path, WatchService watchService) throws IOException {
        // 遍历path文件系统
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE
                        , StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

                return FileVisitResult.CONTINUE;
            }
        });
    }


}
