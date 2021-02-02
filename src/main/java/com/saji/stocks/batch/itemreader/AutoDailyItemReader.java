package com.saji.stocks.batch.itemreader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("autoDailyItemReader")
@StepScope
public class AutoDailyItemReader implements ItemReader<String> {

    @Value("#{stepExecutionContext['itemList']}")
    private List<String> messageList = new ArrayList<>();

    private ListItemReader<String> delegate = null;

    @Override
    public String read() throws Exception {
        if (null == delegate) {
            delegate = new ListItemReader<>(messageList);
        }
        return delegate.read();
    }

    public void setMessageList(final List<String> messageList) {
        this.messageList = messageList;
    }

}
