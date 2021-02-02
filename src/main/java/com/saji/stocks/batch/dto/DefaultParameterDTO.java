/**
 *
 */
package com.saji.stocks.batch.dto;

/**
 * @author sajeth
 *
 */
public class DefaultParameterDTO {
    private String jobName;
    private String key;
    private String value;

    /**
     * @param jobName
     * @param key
     * @param value
     */
    public DefaultParameterDTO(final String jobName, final String key, final String value) {
        this.jobName = jobName;
        this.key = key;
        this.value = value;
    }

    public final String getJobName() {
        return jobName;
    }

    public final void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public final String getKey() {
        return key;
    }

    public final void setKey(final String key) {
        this.key = key;
    }

    public final String getValue() {
        return value;
    }

    public final void setValue(final String value) {
        this.value = value;
    }

}
