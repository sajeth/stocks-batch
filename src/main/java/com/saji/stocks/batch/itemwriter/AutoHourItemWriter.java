package com.saji.stocks.batch.itemwriter;

import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component("autoHourItemWriter")
@StepScope
public class AutoHourItemWriter implements ItemWriter<String> {

    private static final Logger log = Logger.getLogger("AutoHourItemWriter");

    @Autowired
    private IStockData iStockData;

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(symbol -> {
            log.info("Processing Hourly updates  for " + symbol);
            try {


            } catch (Exception e) {
                e.printStackTrace();
                log.log(java.util.logging.Level.SEVERE, e.getMessage() + " " + symbol);
            }
        });

    }

}
