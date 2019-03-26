package com.felix.thread;

import java.io.File;
import java.nio.file.*;

public class ThreadTest {
    public static void main(String[] args) throws Exception {

        String rootPath = System.getProperty("user.dir");
        Path path = Paths.get(rootPath + File.separator + "file-operator" + File.separator + "conf");
        WatchService watchService = path.getFileSystem().newWatchService();

        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        while(true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent watchEvent : watchKey.pollEvents()) {
                System.out.println("current operator is : " + watchEvent.context().toString() + "===" + watchEvent.kind());
            }
            watchKey.reset();
        }



    }
}
