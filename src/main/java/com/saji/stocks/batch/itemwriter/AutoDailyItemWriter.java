package com.saji.stocks.batch.itemwriter;


import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component("autoDailyItemWriter")
@StepScope
public class AutoDailyItemWriter implements ItemWriter<String> {

    private static final Logger log = Logger.getLogger("AutoDailyItemWriter");

    @Autowired
    private IStockData iStockData;

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(symbol -> {
            log.info("Processing Daily id " + symbol + ",size=" + items.size() + ",symbols=" + symbol);
            try {
                iStockData.updateDailyStock(symbol);

            } catch (Exception e) {
                e.printStackTrace();
                log.log(java.util.logging.Level.SEVERE, e.getMessage() + " " + symbol);
            }
        });

    }

}
