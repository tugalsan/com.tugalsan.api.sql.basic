package com.tugalsan.api.sql.basic.client;

import java.util.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.sql.col.typed.client.*;

public class TGS_SQLBasicConfig {

    public TGS_SQLBasicConfig(CharSequence tableName, CharSequence colNameId, 
            CharSequence colNameParam, CharSequence colNameValue) {
        this.tableName = tableName.toString();
        this.colNameId = colNameId.toString();
        this.colNameParam = colNameParam.toString();
        this.colNameValue = colNameValue.toString();
    }
    public String tableName;
    public String colNameId;
    public String colNameParam;
    public String colNameValue;

    public TGS_SQLBasicConfig setColNameId(CharSequence colNameId) {
        this.colNameId = colNameId.toString();
        return this;
    }

    public TGS_SQLBasicConfig setTableName(CharSequence tableName) {
        this.tableName = tableName.toString();
        return this;
    }

    public TGS_SQLBasicConfig setColNameParam(CharSequence colNameParam) {
        this.colNameParam = colNameParam.toString();
        return this;
    }

    public TGS_SQLBasicConfig setColNameValue(CharSequence colNameValue) {
        this.colNameValue = colNameValue.toString();
        return this;
    }

    public List<TGS_SQLColTyped> getColumnTypes() {
        return TGS_ListUtils.of(
                new TGS_SQLColTyped(colNameId),
                new TGS_SQLColTyped(colNameParam),
                new TGS_SQLColTyped(colNameValue)
        );
    }
}
