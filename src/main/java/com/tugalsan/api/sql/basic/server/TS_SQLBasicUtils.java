package com.tugalsan.api.sql.basic.server;

import module com.tugalsan.api.list;
import module com.tugalsan.api.sql.basic;
import module com.tugalsan.api.os;
import module com.tugalsan.api.tuple;
import module com.tugalsan.api.sql.conn;
import module com.tugalsan.api.sql.delete;
import module com.tugalsan.api.sql.insert;
import module com.tugalsan.api.sql.max;
import module com.tugalsan.api.sql.select;
import module com.tugalsan.api.sql.tbl;
import module com.tugalsan.api.sql.update;
import module com.tugalsan.api.string;
import module com.tugalsan.api.url;
import java.nio.file.*;
import java.util.*;

public class TS_SQLBasicUtils {

    public static boolean createCommonTableIfNotExists(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg) {
        return TS_SQLTblUtils.createIfNotExists(anchor, false, cfg.tableName, cfg.getColumnTypes());
    }

    public static TS_SQLConnStmtUpdateResult del(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag) {
        return TS_SQLDeleteUtils.delete(anchor, cfg.tableName)
                .whereConditionAnd(conditions -> conditions.strEq(cfg.colNameParam, tag));
    }

    public static String strList_addItm(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence... newValues) {
        var lst = strList_getLst(anchor, cfg, tag);
        Arrays.stream(newValues).forEach(newValue -> {
            var present = lst.stream().anyMatch(itm -> Objects.equals(itm, newValue));
            if (present) {
                return;
            }
            lst.add(newValue.toString());
        });
        return strList_setLst(anchor, cfg, tag, lst);
    }

    public static String strList_delItm(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence... newValues) {
        var lst = strList_getLst(anchor, cfg, tag);
        Arrays.stream(newValues).forEachOrdered(newValue -> lst.remove(newValue.toString()));
        return strList_setLst(anchor, cfg, tag, lst);
    }

    public static String strList_setLst(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence... newValues) {
        return strList_setLst(anchor, cfg, tag, Arrays.stream(newValues).map(cs -> cs.toString()).toList());
    }

    public static String strList_setLst(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, List<String> newValues) {
        return setStr(anchor, cfg, tag, TGS_StringUtils.cmn().toString(newValues, "\n"));
    }

    public static List<String> strList_getLst(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag) {
        var str = getStr(anchor, cfg, tag, null);
        if (str == null) {
            return TGS_ListUtils.of();
        }
        return TGS_StringUtils.jre().toList(str, "\n");
    }

    public static String getStr(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence defaultString) {
        var valStr = TS_SQLSelectUtils.select(anchor, cfg.tableName).columns(cfg.colNameValue)
                .whereConditionAnd(condition -> condition.strEq(cfg.colNameParam, tag))
                .groupNone().orderNone().rowIdxOffsetNone().rowSizeLimitNone().getStr();
        if (valStr == null) {
            if (defaultString == null) {
                return null;
            }
            valStr = defaultString.toString();
            var rowId = TS_SQLMaxUtils.max(anchor, cfg.tableName, cfg.colNameId).whereConditionNone().nextId();
            TS_SQLInsertUtils.insert(anchor, cfg.tableName).valObj(rowId, tag, valStr);
        }
        return valStr;
    }

    public static String setStr(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, CharSequence newValue) {
        var newValueStr = newValue.toString();
        var oldValueStr = getStr(anchor, cfg, tag, newValueStr);
        if (Objects.equals(oldValueStr, newValueStr)) {
            return newValueStr;
        }
        TS_SQLUpdateUtils.update(anchor, cfg.tableName)
                .set(set -> set.add(new TGS_Tuple2(cfg.colNameValue, newValue)))
                .whereConditionAnd(conditions -> conditions.strEq(cfg.colNameParam, tag));
        return newValueStr;
    }

    public static TGS_Url getUrl(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, TGS_Url defaultURL) {
        var defaultURLStr = defaultURL == null ? null : defaultURL.toString();
        var urlStr = getStr(anchor, cfg, tag, defaultURLStr);
        return TGS_Url.of(urlStr);
    }

    public static TGS_Url setUrl(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, TGS_Url newURL) {
        setStr(anchor, cfg, tag, newURL.toString());
        return newURL;
    }

    public static Path getPath(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, String tag, Path defaultPath) {
        if (defaultPath == null) {
            var resultStrValue = getStr(anchor, cfg, tag, null);
            if (resultStrValue == null) {
                return null;
            }
            return Path.of(resultStrValue);
        }
        var isWin = TS_OsPlatformUtils.isWindows();
        var defaultPathStr = isWin ? defaultPath.toString().replace("\\", "/") : defaultPath.toString();
        var resultStrValue = getStr(anchor, cfg, tag, defaultPathStr);
        if (resultStrValue == null) {
            return null;
        }
        if (isWin) {
            resultStrValue = resultStrValue.replace("/", "\\");
        }
        return Path.of(resultStrValue);
    }

    public static Path setPath(TS_SQLConnAnchor anchor, TGS_SQLBasicConfig cfg, CharSequence tag, Path newPath) {
        setStr(anchor, cfg, tag, newPath.toAbsolutePath().toString().replace("\\", "/"));
        return newPath;
    }
}
