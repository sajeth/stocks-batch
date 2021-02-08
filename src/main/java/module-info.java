/**
 * @author saji 06-Apr-2018
 */
module stocks.batch {
    requires transitive stocks.core;
    requires transitive stocks.mongo;
    requires transitive stocks.entities;
    requires transitive stocks.analysis;
    requires spring.context;
    requires spring.beans;
    requires spring.batch.core;
    requires spring.retry;
    requires spring.tx;
    requires spring.orm;
    requires spring.web;
    requires spring.core;
    requires java.logging;
    requires spring.context.support;
    requires quartz;
    requires java.sql;
    requires spring.batch.infrastructure;
    requires org.apache.commons.lang3;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    opens com.saji.stocks.batch.services to spring.core;
    opens com.saji.stocks.batch to spring.core;
    opens com.saji.stocks.batch.config to spring.core;
    opens com.saji.stocks.batch.config.jobs to spring.core;
    opens com.saji.stocks.batch.itemreader to spring.core;
    opens com.saji.stocks.batch.itemwriter to spring.core;
    opens com.saji.stocks.batch.partitoner to spring.core;
    opens com.saji.stocks.batch.partitioner to spring.core;

    exports com.saji.stocks.batch.config.jobs to spring.beans, spring.context;
    exports com.saji.stocks.batch to spring.beans, spring.context;
    exports com.saji.stocks.batch.config to spring.beans, spring.context;
    exports com.saji.stocks.batch.partitoner to spring.beans, spring.context;
    exports com.saji.stocks.batch.services to spring.beans, spring.context;
    exports com.saji.stocks.batch.dto;
    exports com.saji.stocks.batch.quartz to spring.context.support;
    exports com.saji.stocks.batch.listener to spring.batch.infrastructure;
    exports com.saji.stocks.batch.itemreader to spring.beans;
    exports com.saji.stocks.batch.itemwriter to spring.beans;
}