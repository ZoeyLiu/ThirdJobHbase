package com.zoey.manager;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author yining.liu@woqutech.com
 * @Date 2021/8/1 5:41 下午
 * @Description:
 */
public class HBaseConn {
    private static final String zookeeperUrl = "47.101.204.23:2181,47.101.216.12:2181,47.101.206.249:2181";

    private Configuration configuration;
    private Connection connection;

    private static class HBaseConnInstance {
        private static final HBaseConn instance = new HBaseConn();
    }

    public static HBaseConn getInstance() {
        return HBaseConnInstance.instance;
    }

    private HBaseConn() {
        try {
            if (configuration == null) {
                configuration = HBaseConfiguration.create();
//                configuration.set("hbase.zookeeper.property.clientPort", "2181");
//                configuration.set("hbase.zookeeper.quorum", "47.101.204.23,47.101.216.12,47.101.206.249");
                configuration.set("hbase.zookeeper.quorum", zookeeperUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        if (connection == null || connection.isClosed()) {
            try {
                connection = ConnectionFactory.createConnection(configuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public Connection getHBaseConn() {
        return getInstance().getConnection();
    }

    public void closeConn() {
        if (getInstance().connection != null) {
            try {
                getInstance().connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
