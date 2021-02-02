/**
 *
 */
package com.saji.stocks.batch.services.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sajeth
 *
 */
public class Status {
    private String name;
    private String description;
    private List<Instance> instances;

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final List<Instance> getInstances() {
        return instances;
    }

    public final void setInstances(final List<Instance> instances) {
        this.instances = instances;
    }

    public void addInstance(final Instance instance) {
        if (Optional.ofNullable(instance).isPresent()) {
            this.instances.add(instance);
        } else {
            this.instances = new ArrayList<>();
        }
    }
}
