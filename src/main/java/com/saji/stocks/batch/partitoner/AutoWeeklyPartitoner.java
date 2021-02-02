package com.saji.stocks.batch.partitoner;

import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
import com.saji.stocks.core.services.stock.IStock;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("autoWeeklyPartitioner")
@StepScope
public class AutoWeeklyPartitoner extends ListItemReaderPartitioner {

    private static final String STEP_NAME = "autoWeekly-";

    @Autowired
    IStock iStocks;

    @SuppressWarnings("unchecked")
    @Override
    protected List<String> fetchItemList() {
        return iStocks.findWeeklyStocks();
    }

    @Override
    protected String buildStepName(final int partitionNumber) {
        return STEP_NAME + partitionNumber;
    }

}
