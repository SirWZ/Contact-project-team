package com.ws.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ClassName：SetACL
 * </p>
 * <p>
 * Description：${DESCRIPTION}
 * </p>
 * <p>
 * User：sun
 * </p>
 * <p>
 * CreateTime：2019-04-08 16:46
 * </p>
 * <p>
 * Modify：
 * </p>
 * <p>
 * ModifyTime：
 * </p>
 * <p>
 * Commont：
 * </p>
 * <p>
 * Copyright (c)
 * </p>
 *
 * @author sun
 * @version 1.0.0
 */
public class SetACL {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("10.101.127.166:2181", 60000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("监控所有被触发的事件:EVENT:" + event.getType());
            }
        });

        Id id = new Id("ip", "10.101.127.141/191");
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);
        ArrayList<ACL> acls = new ArrayList<>();
        acls.add(acl);
        zk.setACL("/test", acls, -1);


        List<String> list = zk.getChildren("/", null);
        list.forEach(path -> {
                    System.out.println(path);

                }
        );

    }
}
