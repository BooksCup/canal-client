package com.bc.canal.client.persist;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bc.canal.client.cons.Constants;
import org.apache.log4j.Logger;

import com.bc.canal.client.CanalClient;
import com.bc.canal.client.parser.DataParser;

/**
 * @author zhou
 */
public class DataPersist {
    private static final Logger logger = Logger.getLogger(DataParser.class);

    public static void saveDataLogs(String rowData) {
        // 日志格式
        String pattern;

        // 年
        if (Constants.CANAL_BINLOG_FORMAT_YEAR.equalsIgnoreCase(CanalClient.canalBinlogFilename)) {
            pattern = "yyyy";
        }
        // 月
        else if (Constants.CANAL_BINLOG_FORMAT_MONTH.equalsIgnoreCase(CanalClient.canalBinlogFilename)) {
            pattern = "yyyyMM";
        }
        // 日
        else if (Constants.CANAL_BINLOG_FORMAT_DAY.equalsIgnoreCase(CanalClient.canalBinlogFilename)) {
            pattern = "yyyyMMdd";
        }
        // 时
        else if (Constants.CANAL_BINLOG_FORMAT_HOUR.equalsIgnoreCase(CanalClient.canalBinlogFilename)) {
            pattern = "yyyyMMddHH";
        }
        // 分
        else if (Constants.CANAL_BINLOG_FORMAT_MINUTE.equalsIgnoreCase(CanalClient.canalBinlogFilename)) {
            pattern = "yyyyMMddHHmm";
        } else {
            // 默认格式"年月日"
            pattern = "yyyyMMdd";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String timeStamp = formatter.format(new Date());
        String dataLogName = CanalClient.canalBinlogDir + "/binlog_" + timeStamp + ".log";

        FileWriter writer = null;
        try {
            try {
                File dir = new File(CanalClient.canalBinlogDir);
                // 日志目录不存在则创建
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                writer = new FileWriter(dataLogName, true);
                writer.write(rowData + "\r\n");
                writer.flush();
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            logger.error("Failed to write data logs(" + dataLogName + "), error: " + e.getMessage());
        }
    }
}
