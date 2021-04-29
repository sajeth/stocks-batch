package com.saji.stocks.batch.partitoner;

import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("autoMonthlyPartitioner")
@StepScope
public class AutoMonthlyPartitioner extends ListItemReaderPartitioner {

    private static final String STEP_NAME = "autoMonthlyUpdate-";

    @Autowired
    IStockData iStockData;

    @Override
    protected List<String> fetchItemList() {
        return iStockData.getStocks();
    }

    @Override
    protected String buildStepName(final int partitionNumber) {
        return STEP_NAME + partitionNumber;
    }

}
