package com.saji.stocks.batch.partitioner;

import java.util.Date;

public interface FetchWindow {

    Date getStartDate();

    Date getEndDate();
}
