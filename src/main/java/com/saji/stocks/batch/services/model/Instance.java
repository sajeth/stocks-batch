/**
 *
 */
package com.saji.stocks.batch.services.model;

import java.util.Date;

/**
 * @author sajeth
 *
 */
public class Instance {
    private int priority;
    private String status;
    private boolean fireable;
    private boolean running;
    private int misfireCode;
    private Date previousFireTime;
    private Date finalFireTime;
    private Date nextFireTime;

    public final int getPriority() {
        return priority;
    }

    public final void setPriority(final int priority) {
        this.priority = priority;
    }

    public final String getStatus() {
        return status;
    }

    public final void setStatus(String status) {
        this.status = status;
    }

    public final boolean isFireable() {
        return fireable;
    }

    public final void setFireable(final boolean fireable) {
        this.fireable = fireable;
    }

    public final boolean isRunning() {
        return running;
    }

    public final void setRunning(final boolean running) {
        this.running = running;
    }

    public final int getMisfireCode() {
        return misfireCode;
    }

    public final void setMisfireCode(final int misfireCode) {
        this.misfireCode = misfireCode;
    }

    public final Date getPreviousFireTime() {
        return previousFireTime;
    }

    public final void setPreviousFireTime(final Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public final Date getFinalFireTime() {
        return finalFireTime;
    }

    public final void setFinalFireTime(final Date finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    public final Date getNextFireTime() {
        return nextFireTime;
    }

    public final void setNextFireTime(final Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

}
