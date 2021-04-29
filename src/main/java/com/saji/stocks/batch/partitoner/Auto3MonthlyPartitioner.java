package com.saji.stocks.batch.partitoner;

import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
import com.saji.stocks.batch.util.FileUtil;
import com.saji.stocks.mongo.pojos.StockData;
import com.saji.stocks.mongo.services.IService;
import com.saji.stocks.mongo.services.IStockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("auto3MonthlyPartitioner")
@StepScope
public class Auto3MonthlyPartitioner extends ListItemReaderPartitioner {

    private static final String STEP_NAME = "auto3MonthlyUpdate-";
    @Autowired
    IStockData iStockData;
    @Autowired
    IService iService;

    @Override
    protected List<StockData> fetchItemList() {
        try {
            if (iStockData.getStocks().isEmpty()) {
                return FileUtil.parseFile("Equity.csv");
            } else {
                return FileUtil.parseFile("Equity.csv").stream().filter(
                        val -> !iService.isStockPresent(val.getSymbol())
                ).collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected String buildStepName(final int partitionNumber) {
        return STEP_NAME + partitionNumber;
    }

}
