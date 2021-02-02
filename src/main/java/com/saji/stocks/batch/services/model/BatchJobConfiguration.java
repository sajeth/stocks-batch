/**
 *
 */
package com.saji.stocks.batch.services.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author sajeth
 *
 */
public class BatchJobConfiguration implements Serializable, Comparator<BatchJobConfiguration> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String jobName;
    private Long maxRuntimeMinutes;

    /**
     * @param jobName
     * @param maxRuntimeMinutes
     */
    public BatchJobConfiguration(final String jobName, final Long maxRuntimeMinutes) {
        this.jobName = jobName;
        this.maxRuntimeMinutes = maxRuntimeMinutes;
    }

    public final String getJobName() {
        return jobName;
    }

    public final void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public final Long getMaxRuntimeMinutes() {
        return maxRuntimeMinutes;
    }

    public final void setMaxRuntimeMinutes(final Long maxRuntimeMinutes) {
        this.maxRuntimeMinutes = maxRuntimeMinutes;
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder hashBuilder = new HashCodeBuilder();
        hashBuilder.append(jobName);
        hashBuilder.append(maxRuntimeMinutes);
        return hashBuilder.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BatchJobConfiguration) {
            return this.getJobName().equalsIgnoreCase(((BatchJobConfiguration) obj).getJobName())
                    && this.getMaxRuntimeMinutes().equals(((BatchJobConfiguration) obj).getMaxRuntimeMinutes());
        }
        return false;
    }

    @Override
    public int compare(final BatchJobConfiguration o1, final BatchJobConfiguration o2) {
        return o1.getJobName().compareTo(o2.getJobName()) != 0 ? o1.getJobName().compareTo(o2.getJobName())
                : o1.getMaxRuntimeMinutes().compareTo(o2.getMaxRuntimeMinutes());
//        int compareDiff=o1.getJobName().compareTo(o2.getJobName());
//        if(compareDiff==0) {
//            compareDiff=o1.getMaxRuntimeMinutes().compareTo(o2.getMaxRuntimeMinutes());
//        }
//        return compareDiff;
    }

}
