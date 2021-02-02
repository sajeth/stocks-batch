//package com.saji.stocks.batch.itemwriter;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.saji.stocks.batch.dto.StockDTO;
//import com.saji.stocks.cache.services.IStockData;
//import com.saji.stocks.services.stock.IStock;
//
//@Component("auto30MinItemWriter")
//@StepScope
//public class Auto30MinItemWriter implements ItemWriter<StockDTO> {
//
//	private static final Logger log = Logger.getLogger("Auto30MinItemWriter");
//	
//	@Autowired private IStock iStock;
//	@Autowired private IStockData iStockData;
//	
//	@Override
//	public void write(List<? extends StockDTO> items) throws Exception {
//			
//	}
//	
////	@AfterChunk
////	public void doLater() {
////		
////	}
//
//}
