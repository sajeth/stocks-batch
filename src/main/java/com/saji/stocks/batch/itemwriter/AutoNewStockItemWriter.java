package com.saji.stocks.batch.itemwriter;

import com.saji.stocks.mongo.services.IStockData;
import com.saji.stocks.redis.services.IRedis;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component("autoNewStockItemWriter")
@StepScope
public class AutoNewStockItemWriter implements ItemWriter<String> {

    private static final Logger log = Logger.getLogger("AutoNewStockItemWriter");

    @Autowired
    IRedis iRedis;
    @Autowired
    private IStockData iStockData;

    @Override
    public void write(List<? extends String> items) throws Exception {

        items.stream().forEach(symbol -> {
            try {
                log.info("Processing new symbols " + symbol + ",size=" + items.size() + ",symbol=" + symbol);
                iStockData.populateStock(symbol);
            } catch (Exception e) {
                log.log(java.util.logging.Level.SEVERE, e.getMessage() + " " + symbol);
            }
        });//items.clear();

    }
}
