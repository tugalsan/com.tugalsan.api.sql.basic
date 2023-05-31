module com.tugalsan.api.sql.basic {
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.tuple;
    requires com.tugalsan.api.sql.col.typed;
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.sql.conn;
    requires com.tugalsan.api.sql.insert;
    requires com.tugalsan.api.sql.max;
    requires com.tugalsan.api.sql.select;
    requires com.tugalsan.api.sql.tbl;
    requires com.tugalsan.api.sql.update;
    requires com.tugalsan.api.sql.delete;
    requires com.tugalsan.api.sql.where;
  exports com.tugalsan.api.sql.basic.client;
  exports com.tugalsan.api.sql.basic.server;
}
