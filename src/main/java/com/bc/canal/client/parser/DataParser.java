package com.bc.canal.client.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bc.canal.client.cons.Constants;
import com.bc.canal.client.handler.MqSenderHandler;
import com.bc.canal.client.mq.MqSender;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.bc.canal.client.CanalClient;
import com.bc.canal.client.mq.KafkaSender;
import com.bc.canal.client.mq.RabbitmqSender;
import com.bc.canal.client.mq.RedisSender;
import com.bc.canal.client.persist.DataPersist;

/**
 * @author zhou
 */
public class DataParser {
    private static final Logger logger = Logger.getLogger(DataParser.class);

    public static void parse(List<Entry> entries) {
        List<String> dataList = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = formatter.format(new Date());

        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            EventType eventType = rowChange.getEventType();
            StringBuffer headerBuffer = new StringBuffer().append("{\"binlog\":\"").append(entry.getHeader().getLogfileName()).
                    append(":").append(entry.getHeader().getLogfileOffset()).append("\",").
                    append("\"db\":\"").append(entry.getHeader().getSchemaName()).append("\",").
                    append("\"table\":\"").append(entry.getHeader().getTableName()).append("\",");

            for (RowData rowData : rowChange.getRowDatasList()) {
                String eventTypeStr = "\"eventType\":\"" + eventType + "\",";
                String before = "\"\"";
                String after;

                if (eventType == EventType.DELETE) {
                    after = columnsListToJsonStr(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    after = columnsListToJsonStr(rowData.getAfterColumnsList());
                } else {
                    //update
                    before = columnsListToJsonStr(rowData.getBeforeColumnsList());
                    after = columnsListToJsonStr(rowData.getAfterColumnsList());
                }

                StringBuffer rowBuffer = new StringBuffer().append(headerBuffer).append(eventTypeStr).
                        append("\"before\":").append(before).
                        append(",\"after\":").append(after).
                        append(",\"time\":\"").append(timeStr).append("\"}");
                String rowStr = rowBuffer.toString();
                //打印rowData
                if (CanalClient.canalPrint) {
                    logger.info(rowStr);
                }
                //写日志
                DataPersist.saveDataLogs(rowStr);
                //存list,批量发MQ
                dataList.add(rowStr);
            }
        }

        //发送MQ
        MqSender mqSender = MqSenderHandler.getMqSender(CanalClient.canalMq);
        mqSender.send(dataList);

    }

    private static String columnsListToJsonStr(List<Column> columns) {
        Map<String, String> columnMap = new HashMap<>();
        for (Column column : columns) {
            String columnName = column.getName();
            String columnValue = column.getValue();
            columnMap.put(columnName, columnValue);
        }
        return JSON.toJSONString(columnMap);
    }
}
