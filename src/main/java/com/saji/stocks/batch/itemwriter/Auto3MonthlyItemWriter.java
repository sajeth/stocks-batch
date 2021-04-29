package com.saji.stocks.batch.itemwriter;

import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component("auto3MonthlyItemWriter")
@StepScope
public class Auto3MonthlyItemWriter implements ItemWriter<StockData> {

    private static final Logger log = Logger.getLogger("Auto3MonthlyItemWriter");


    @Autowired
    private IStockData iStockData;

    @Override
    public void write(List<? extends StockData> items) throws Exception {

        items.forEach(val -> {
            log.info("Processing 3 months updates for " + val.getSymbol());
            try {
                iStockData.updateThreeMonthStock(val);
            } catch (Exception e) {
                log.log(java.util.logging.Level.SEVERE, e.getMessage() + " " + val.getSymbol());
            }
        });
    }

}
