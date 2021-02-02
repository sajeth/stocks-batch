/**
 *
 */
package com.saji.stocks.batch.services.model;

import java.util.List;

/**
 * @author sajeth
 *
 */

public class Statuses {
    private List<Status> statuses;

    public Statuses(final List<Status> statuses) {
        this.statuses = statuses;
    }

    public final List<Status> getStatuses() {
        return statuses;
    }

    public final void setStatuses(final List<Status> statuses) {
        this.statuses = statuses;
    }

}
