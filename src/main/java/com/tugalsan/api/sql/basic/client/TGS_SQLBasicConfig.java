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
                TGS_SQLColTyped.of(colNameId),
                TGS_SQLColTyped.of(colNameParam),
                TGS_SQLColTyped.of(colNameValue)
        );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.tableName);
        hash = 53 * hash + Objects.hashCode(this.colNameId);
        hash = 53 * hash + Objects.hashCode(this.colNameParam);
        hash = 53 * hash + Objects.hashCode(this.colNameValue);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TGS_SQLBasicConfig other = (TGS_SQLBasicConfig) obj;
        if (!Objects.equals(this.tableName, other.tableName)) {
            return false;
        }
        if (!Objects.equals(this.colNameId, other.colNameId)) {
            return false;
        }
        if (!Objects.equals(this.colNameParam, other.colNameParam)) {
            return false;
        }
        return Objects.equals(this.colNameValue, other.colNameValue);
    }

    @Override
    public String toString() {
        return TGS_SQLBasicConfig.class.getSimpleName() + "{" + "tableName=" + tableName + ", colNameId=" + colNameId + ", colNameParam=" + colNameParam + ", colNameValue=" + colNameValue + '}';
    }
    
    
}
