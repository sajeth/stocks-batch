package com.saji.stocks.batch.itemwriter;


import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component("autoWeeklyItemWriter")
@StepScope
public class AutoWeeklyItemWriter implements ItemWriter<String> {

    private static final Logger log = Logger.getLogger("AutoWeeklyItemWriter");

    @Autowired
    private IStockData iStockData;

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(symbols -> {
            log.info("Processing weekly id " + symbols + ",size=" + items.size());
            try {
                iStockData.updateWeeklyStock(symbols);
            } catch (Exception e) {
                log.log(java.util.logging.Level.SEVERE, e.getMessage() + " " + symbols);
            }
        });
    }

}
