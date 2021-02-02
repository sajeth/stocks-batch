package com.saji.stocks.batch.listener;

import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemFailureLoggerListener<I, O> {

    private static final Logger log = Logger.getLogger("BatchSkipPolicy");

    @OnReadError
    public void onReadError(final Exception ex) {
        log.log(Level.SEVERE, "Exception occured in batch on read:" + ex.getMessage());
    }

    @OnWriteError
    public void onWriteError(final Exception ex, final List<? extends O> items) {
        log.log(Level.SEVERE, "Exception occured in batch on write for " + items + " : " + ex.getMessage());
    }
}
