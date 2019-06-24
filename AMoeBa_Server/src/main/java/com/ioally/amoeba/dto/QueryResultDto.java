package com.ioally.amoeba.dto;

import java.util.List;
import java.util.Map;

public class QueryResultDto {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private List<String> columnNames;

    /**
     * 数据，k=列名，v=字段值
     */
    private List<Map<String, Object>> rowDate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<Map<String, Object>> getRowDate() {
        return rowDate;
    }

    public void setRowDate(List<Map<String, Object>> rowDate) {
        this.rowDate = rowDate;
    }
}
