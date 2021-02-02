//package com.saji.stocks.batch.itemreader;
//
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.support.ListItemReader;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component("auto30MinItemReader")
//@StepScope
//public class Auto30MinItemReader implements ItemReader<StockDTO> {
//
//    @Value("#{stepExecutionContext['itemList']}")
//    private List<StockDTO> messageList = new ArrayList<StockDTO>();
//
//    private ListItemReader<StockDTO> delegate = null;
//
//    @Override
//    public StockDTO read() throws Exception {
//        if (null != delegate) {
//            delegate = new ListItemReader<>(messageList);
//        }
//        return delegate.read();
//    }
//
//    public void setMessageList(final List<StockDTO> messageList) {
//        this.messageList = messageList;
//    }
//
//}
