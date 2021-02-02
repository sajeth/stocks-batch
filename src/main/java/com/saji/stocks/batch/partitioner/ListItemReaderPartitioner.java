/**
 *
 */
package com.saji.stocks.batch.partitioner;

import com.saji.stocks.batch.util.Partition;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public abstract class ListItemReaderPartitioner implements Partitioner {
    protected abstract <T extends List<? extends Serializable>> T fetchItemList();

    protected abstract String buildStepName(final int partitionNumber);


    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        final Map<String, ExecutionContext> partitionMap = new HashMap<>();
        final List<? extends Serializable> itemsToProcess = fetchItemList();
        AtomicInteger atomicInteger = new AtomicInteger();
//     
        Partition.ofSize(itemsToProcess, gridSize).stream().forEach(
                val -> {
                    final ExecutionContext context = new ExecutionContext();
                    context.put("itemList", val);
                    partitionMap.put(buildStepName(atomicInteger.getAndIncrement()), context);
                }
        );
        return partitionMap;
    }

}
