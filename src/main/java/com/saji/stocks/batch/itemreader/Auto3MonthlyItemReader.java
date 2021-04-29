package com.saji.stocks.batch.itemreader;

import com.saji.stocks.mongo.pojos.StockData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("auto3MonthlyItemReader")
@StepScope
public class Auto3MonthlyItemReader implements ItemReader<StockData> {

    @Value("#{stepExecutionContext['itemList']}")
    private List<StockData> messageList = new ArrayList<>();

    private ListItemReader<StockData> delegate;

    @Override
    public StockData read() throws Exception {

        if (null == delegate) {
            delegate = new ListItemReader<>(messageList);
        }
        return delegate.read();
    }

    public void setMessageList(final List<StockData> messageList) {
        this.messageList = messageList;
    }

}
