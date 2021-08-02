package com.zoey.utils;

import com.zoey.manager.HBaseConn;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author yining.liu@woqutech.com
 * @Date 2021/8/1 5:50 下午
 * @Description:
 */
public class HBaseUtil {
    private HBaseUtil() {
    }

    private static class HHBaseUtilInstance {
        private static final HBaseUtil instance = new HBaseUtil();
    }

    public static HBaseUtil getInstance() {
        return HHBaseUtilInstance.instance;
    }

    /**
     * 创建namespace
     *
     * @param namespace 创建namespace名称
     * @return
     */
    public boolean createNamespace(String namespace) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.getInstance().getHBaseConn().getAdmin()) {
            NamespaceDescriptor[] listNamespaceDescriptors = admin.listNamespaceDescriptors();
            System.out.println(Arrays.toString(listNamespaceDescriptors));
            for (NamespaceDescriptor namespaceDescriptor : listNamespaceDescriptors) {
                if (namespaceDescriptor.getName().equals(namespace)) {
                    return false;
                }
            }
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
            admin.createNamespace(namespaceDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 创建表
     *
     * @param tableName 创建表的表名称
     * @param cfs       列簇的集合
     * @return
     */
    public boolean createTable(String tableName, List<String> cfs) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.getInstance().getHBaseConn().getAdmin()) {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                return false;
            }

            TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            cfs.forEach(columnFamily -> {
                ColumnFamilyDescriptorBuilder cfDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                cfDescriptorBuilder.setMaxVersions(1);
                ColumnFamilyDescriptor familyDescriptor = cfDescriptorBuilder.build();
                tableDescriptor.setColumnFamily(familyDescriptor);
            });
            admin.createTable(tableDescriptor.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除表
     *
     * @param tableName 表名称
     * @return
     */
    public boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.getInstance().getHBaseConn().getAdmin()) {
            if (!admin.tableExists(TableName.valueOf(tableName))) {
                return false;
            }
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 插入数据
     *
     * @param tableName
     * @param rowkey
     * @param cfName
     * @param qualifer
     * @param data
     * @return
     */
    public boolean putRow(String tableName, String rowkey, String cfName, String qualifer, String data) {
        try (Table table = HBaseConn.getInstance().getHBaseConn().getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowkey));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifer), Bytes.toBytes(data));
            table.put(put);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询单条数据
     *
     * @param tableName
     * @param rowkey
     * @return
     */
    public Result getRow(String tableName, String rowkey) {
        try (Table table = HBaseConn.getInstance().getHBaseConn().getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowkey));
            return table.get(get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * scan扫描数据，
     *
     * @param tableName
     * @return
     */
    public ResultScanner getScanner(String tableName) {
        try (Table table = HBaseConn.getInstance().getHBaseConn().getTable(TableName.valueOf(tableName))) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            ResultScanner results = table.getScanner(scan);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除行
     *
     * @param tableName
     * @param rowkey
     * @return
     */
    public boolean deleteRow(String tableName, String rowkey) {
        try (Table table = HBaseConn.getInstance().getHBaseConn().getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowkey));
            table.delete(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除列簇
     *
     * @param tableName
     * @param cfName
     * @return
     */
    public boolean deleteColumnFamily(String tableName, String cfName) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.getInstance().getHBaseConn().getAdmin()) {
            admin.deleteColumn(TableName.valueOf(tableName), Bytes.toBytes(cfName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除列
     *
     * @param tableName
     * @param cfName
     * @return
     */
    public boolean deleteQualifier(String tableName, String rowkey, String cfName, String qualiferName) {
        try (Table table = HBaseConn.getInstance().getHBaseConn().getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowkey));
            delete.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualiferName));
            table.delete(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

