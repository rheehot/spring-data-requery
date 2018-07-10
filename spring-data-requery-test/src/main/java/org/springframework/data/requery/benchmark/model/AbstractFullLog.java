package org.springframework.data.requery.benchmark.model;

import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Date;
import java.util.Objects;

/**
 * AbstractFullLog
 *
 * @author debop@coupang.com
 */
@Getter
@Entity(model = "Benchmark", stateless = true)
public abstract class AbstractFullLog extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 4099799937231270510L;

    @Key
    @Generated
    protected Long id;

    protected Date createAt;

    protected String systemId;

    protected String systemName;

    protected Integer logLevel;

    protected String threadName;

    protected String loggerName;

    protected String logMessage;

    @Lazy
    protected String errorMessage;

    @Lazy
    protected String stackTrace;

    @Override
    public int hashCode() {
        return Objects.hash(createAt, systemId, logLevel, threadName);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("createAt", createAt)
            .add("systemId", systemId)
            .add("systenName", systemName)
            .add("threadName", threadName)
            .add("logMessage", logMessage);
    }
}
