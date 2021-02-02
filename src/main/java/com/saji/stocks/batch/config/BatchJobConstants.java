package com.saji.stocks.batch.config;

public enum BatchJobConstants {

    INPUT_DATE_FORMAT_BATCH("yyyy-MM-dd"), DEFAULT("DEFAULT"), JOB_NAME("jobName");

    private final String val;

    BatchJobConstants(final String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
