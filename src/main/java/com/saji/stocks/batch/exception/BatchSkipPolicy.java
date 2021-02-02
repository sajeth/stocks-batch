package com.saji.stocks.batch.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BatchSkipPolicy implements SkipPolicy {

    private static final Logger log = Logger.getLogger("BatchSkipPolicy");
    private final Integer maxSkips;
    private final List<Class<?>> skippableCauses;

    public BatchSkipPolicy(final Integer maxSkips, final List<Class<?>> skippableCauses) {
        this.maxSkips = maxSkips;
        this.skippableCauses = skippableCauses;
    }

    @Override
    public boolean shouldSkip(final Throwable t, final int skipCount) throws SkipLimitExceededException {
        boolean flag = false;
        final Throwable rootCause = Optional.ofNullable(ExceptionUtils.getRootCause(t)).orElse(t);
        if (skippableCauses.contains(rootCause.getClass()) && skipCount <= maxSkips) {
            log.log(Level.WARNING, "skipping instance of exception of type " + rootCause.getClass() + " : "
                    + t.getMessage() + " ,skipCount=" + skipCount + "/" + maxSkips);
            flag = true;
        } else if (skippableCauses.contains(rootCause.getClass()) && skipCount > maxSkips) {
            log.log(Level.SEVERE,
                    "skipping instance of exception of type " + rootCause.getClass()
                            + " because skip count was reached " + t.getMessage() + " ,skipCount=" + skipCount + "/"
                            + maxSkips);
        } else {
            log.log(Level.SEVERE, "Non-skippable exception of type " + rootCause.getClass() + " : " + t.getMessage());
        }
        return flag;
    }

}
