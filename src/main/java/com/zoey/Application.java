package com.zoey;

import com.zoey.manager.HBaseConn;
import com.zoey.utils.HBaseUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author yining.liu@woqutech.com
 * @Date 2021/8/1 11:28 下午
 * @Description:
 */
public class Application {
    private static final String TABLE_NAME = "LiuYiNing:student";
    private static final String INFO_CF_NAME = "info";
    private static final String SCORE_CF_NAME = "score";
    private static final String STUDENT_ID_COLUMN_NAME = "student_id";
    private static final String CLASS_COLUMN_NAME = "class";
    private static final String UNDERSTANDING_COLUMN_NAME = "understanding";
    private static final String PROGRAMMING_COLUMN_NAME = "programming";

    public static void main(String[] args) {
        HBaseUtil.getInstance().createNamespace("LiuYiNing");
        HBaseUtil.getInstance().createTable(TABLE_NAME, Arrays.asList(INFO_CF_NAME, SCORE_CF_NAME));
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Tom", INFO_CF_NAME, STUDENT_ID_COLUMN_NAME, "20210000000001");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Tom", INFO_CF_NAME, CLASS_COLUMN_NAME, "1");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Tom", SCORE_CF_NAME, UNDERSTANDING_COLUMN_NAME, "75");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Tom", SCORE_CF_NAME, PROGRAMMING_COLUMN_NAME, "82");

        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jerry", INFO_CF_NAME, STUDENT_ID_COLUMN_NAME, "20210000000002");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jerry", INFO_CF_NAME, CLASS_COLUMN_NAME, "1");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jerry", SCORE_CF_NAME, UNDERSTANDING_COLUMN_NAME, "85");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jerry", SCORE_CF_NAME, PROGRAMMING_COLUMN_NAME, "67");

        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jack", INFO_CF_NAME, STUDENT_ID_COLUMN_NAME, "20210000000003");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jack", INFO_CF_NAME, CLASS_COLUMN_NAME, "2");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jack", SCORE_CF_NAME, UNDERSTANDING_COLUMN_NAME, "80");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Jack", SCORE_CF_NAME, PROGRAMMING_COLUMN_NAME, "80");

        HBaseUtil.getInstance().putRow(TABLE_NAME, "Rose", INFO_CF_NAME, STUDENT_ID_COLUMN_NAME, "20210000000004");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Rose", INFO_CF_NAME, CLASS_COLUMN_NAME, "2");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Rose", SCORE_CF_NAME, UNDERSTANDING_COLUMN_NAME, "60");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "Rose", SCORE_CF_NAME, PROGRAMMING_COLUMN_NAME, "61");

        HBaseUtil.getInstance().putRow(TABLE_NAME, "LiuYiNing", INFO_CF_NAME, STUDENT_ID_COLUMN_NAME, "G20210735010332");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "LiuYiNing", INFO_CF_NAME, CLASS_COLUMN_NAME, "4");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "LiuYiNing", SCORE_CF_NAME, UNDERSTANDING_COLUMN_NAME, "0");
        HBaseUtil.getInstance().putRow(TABLE_NAME, "LiuYiNing", SCORE_CF_NAME, PROGRAMMING_COLUMN_NAME, "0");

        ResultScanner results = HBaseUtil.getInstance().getScanner(TABLE_NAME);
        results.forEach(result -> {
            System.out.println("rowkey == " + Bytes.toString(result.getRow()));
            System.out.println(INFO_CF_NAME + ":" + STUDENT_ID_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println(INFO_CF_NAME + ":" + CLASS_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(CLASS_COLUMN_NAME))));
            System.out.println(SCORE_CF_NAME + ":" + UNDERSTANDING_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println(SCORE_CF_NAME + ":" + PROGRAMMING_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println();
        });

        HBaseUtil.getInstance().deleteRow(TABLE_NAME,"Rose");
        System.out.println("删除数据Rose后的数据：");
        results = HBaseUtil.getInstance().getScanner(TABLE_NAME);
        results.forEach(result -> {
            System.out.println("rowkey == " + Bytes.toString(result.getRow()));
            System.out.println(INFO_CF_NAME + ":" + STUDENT_ID_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println(INFO_CF_NAME + ":" + CLASS_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(CLASS_COLUMN_NAME))));
            System.out.println(SCORE_CF_NAME + ":" + UNDERSTANDING_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println(SCORE_CF_NAME + ":" + PROGRAMMING_COLUMN_NAME + " == " + Bytes.toString(result.getValue(Bytes.toBytes(INFO_CF_NAME), Bytes.toBytes(STUDENT_ID_COLUMN_NAME))));
            System.out.println();
        });

        HBaseConn.getInstance().closeConn();
    }
}
