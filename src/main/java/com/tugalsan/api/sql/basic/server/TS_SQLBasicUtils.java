package com.tugalsan.api.sql.basic.server;

import java.nio.file.*;
import java.util.*;
import com.tugalsan.api.sql.basic.client.*;
import com.tugalsan.api.os.server.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.sql.delete.server.*;
import com.tugalsan.api.sql.insert.server.*;
import com.tugalsan.api.sql.max.server.*;
import com.tugalsan.api.sql.select.server.*;
import com.tugalsan.api.sql.tbl.server.*;
import com.tugalsan.api.sql.update.server.*;
import com.tugalsan.api.string.server.*;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.url.client.*;

public class TS_SQLBasicUtils {

    public static TGS_UnionExcuseVoid createCommonTableIfNotExists(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg) {
        return TS_SQLTblUtils.createIfNotExists(anchor, false, cfg.tableName, cfg.getColumnTypes());
    }

    public static TGS_UnionExcuse<TS_SQLConnStmtUpdateResult> del(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag) {
        return TS_SQLDeleteUtils.delete(anchor, cfg.tableName)
                .whereConditionAnd(conditions -> conditions.strEq(cfg.colNameParam, tag));
    }

    public static TGS_UnionExcuse<List<String>> getStrList(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence defaultString) {
        var str = getStr(anchor, cfg, tag, defaultString);
        if (str.isExcuse()) {
            return str.toExcuse();
        }
        return TGS_UnionExcuse.of(TS_StringUtils.toList(str.value(), "\n"));
    }

    public static TGS_UnionExcuse<String> getStr(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence defaultString) {
        var u_valStr = TS_SQLSelectUtils.select(anchor, cfg.tableName).columns(cfg.colNameValue)
                .whereConditionAnd(condition -> condition.strEq(cfg.colNameParam, tag))
                .groupNone().orderNone().rowIdxOffsetNone().rowSizeLimitNone().getStr();
        if (u_valStr.isPresent()) {
            return u_valStr;
        }
        if (defaultString == null) {
            return u_valStr.toExcuse();
        }
        var valStr = defaultString.toString();
        var rowId = TS_SQLMaxUtils.max(anchor, cfg.tableName, cfg.colNameId).whereConditionNone().nextId();
        var u_insert0 = TS_SQLInsertUtils.insert(anchor, cfg.tableName);
        if (u_insert0.isExcuse()){
            return u_insert0.toExcuse();
        }
        var u_insert1 = u_insert0.value().valObj(rowId, tag, valStr);
        if (u_insert1.isExcuse()){
            return u_insert1.toExcuse();
        }
        return TGS_UnionExcuse.of(valStr);
    }

    public static TGS_UnionExcuse<String> setStr(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence newValue) {
        var newValueStr = newValue.toString();
        var oldValueStr = getStr(anchor, cfg, tag, newValueStr);
        if (Objects.equals(oldValueStr, newValueStr)) {
            return TGS_UnionExcuse.of(newValueStr);
        }
        var u_update = TS_SQLUpdateUtils.update(anchor, cfg.tableName)
                .set(set -> set.add(new TS_SQLUpdateParam(cfg.colNameValue, newValue)))
                .whereConditionAnd(conditions -> conditions.strEq(cfg.colNameParam, tag));
        if (u_update.isExcuse()){
            return u_update.toExcuse();
        }
        return TGS_UnionExcuse.of(newValueStr);
    }

    public static TGS_UnionExcuse<TGS_Url> getUrl(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, TGS_Url defaultURL) {
        var defaultURLStr = defaultURL == null ? null : defaultURL.toString();
        var urlStr = getStr(anchor, cfg, tag, defaultURLStr);
        if (urlStr.isExcuse()){
            return urlStr.toExcuse();
        }
        return TGS_UnionExcuse.of(TGS_Url.of(urlStr.value()));
    }

    public static TGS_UnionExcuse<TGS_Url> setUrl(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, TGS_Url newURL) {
        var u = setStr(anchor, cfg, tag, newURL.toString());
        if (u.isExcuse()){
            return u.toExcuse();
        }
        return TGS_UnionExcuse.of(newURL);
    }

    public static TGS_UnionExcuse<Path> getPath(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, String tag, Path defaultPath) {
        if (defaultPath == null) {
            var resultStrValue = getStr(anchor, cfg, tag, null);
            if (resultStrValue.isExcuse()) {
                return resultStrValue.toExcuse();
            }
            return TGS_UnionExcuse.of(Path.of(resultStrValue.value()));
        }
        var isWin = TS_OsPlatformUtils.isWindows();
        var defaultPathStr = isWin ? defaultPath.toString().replace("\\", "/") : defaultPath.toString();
        var resultStrValue = getStr(anchor, cfg, tag, defaultPathStr);
        if (resultStrValue.isExcuse()) {
                return resultStrValue.toExcuse();
            }
        String f_resultStrValue = resultStrValue.value();
        if (isWin) {
            f_resultStrValue = f_resultStrValue.replace("/", "\\");
        }
        return TGS_UnionExcuse.of(Path.of(f_resultStrValue));
    }

    public static TGS_UnionExcuse<Path> setPath(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, Path newPath) {
        var u = setStr(anchor, cfg, tag, newPath.toAbsolutePath().toString().replace("\\", "/"));
        if (u.isExcuse()){
            return u.toExcuse();
        }
        return TGS_UnionExcuse.of(newPath);
    }
}
