package com.saji.stocks.batch.partitoner;

import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
import com.saji.stocks.core.services.stock.IStock;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("autoDailyPartitoner")
@StepScope
public class AutoDailyPartitoner extends ListItemReaderPartitioner {

    private static final String STEP_NAME = "autoDailyUpdate-";
    @Autowired
    IStock iStocks;

    protected List<String> fetchItemList() {

        return iStocks.findDailyStocks();
    }

    @Override
    protected String buildStepName(final int partitionNumber) {
        return STEP_NAME + partitionNumber;
    }

}
