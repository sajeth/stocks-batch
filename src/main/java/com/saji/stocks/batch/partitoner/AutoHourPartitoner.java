//package com.saji.stocks.batch.partitoner;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.saji.stocks.batch.client.StocksClient;
//import com.saji.stocks.batch.dto.StockDTO;
//import com.saji.stocks.batch.partitioner.ListItemReaderPartitioner;
//import com.saji.stocks.services.stock.IStock;
//
//@Component("autoHourPartitoner")
//@StepScope
//public class AutoHourPartitoner extends ListItemReaderPartitioner {
//
//	private static final String STEP_NAME = "autoHourUpdate-";
//	
//	@Autowired
//	private StocksClient stocksClient;
//	@Override
//	protected List<StockDTO> fetchItemList() {
//		return iStock.listNewStocks().stream().map(stock->new StockDTO(stock)).collect(Collectors.toList());
//	}
//
//	@Override
//	protected String buildStepName(final int partitionNumber) {
//		return STEP_NAME + partitionNumber;
//	}
//
//}
