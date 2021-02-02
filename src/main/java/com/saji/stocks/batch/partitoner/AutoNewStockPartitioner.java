package com.saji.stocks.batch.partitoner;

import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
import com.saji.stocks.core.services.stock.IStock;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("autoNewStockPartitioner")
@StepScope
public class AutoNewStockPartitioner extends ListItemReaderPartitioner {

    private static final String STEP_NAME = "autoNewStock-";

    @Autowired
    IStock iStocks;

    @Override
    protected List<String> fetchItemList() {
        return iStocks.listNewStocks();
    }

    @Override
    protected String buildStepName(final int partitionNumber) {
        return STEP_NAME + partitionNumber;
    }
}
