package me.johnniang.ncov.job;

import java.util.List;
import lombok.Getter;
import me.johnniang.ncov.model.TimeLine;
import org.springframework.context.ApplicationEvent;

/**
 * @author johnniang
 */
@Getter
public class UpdatedMessageEvent extends ApplicationEvent {

    private final List<TimeLine> timeLines;

    private final long latestTimestamp;

    public UpdatedMessageEvent(Object source, List<TimeLine> lines, long timestamp) {
        super(source);
        timeLines = lines;
        latestTimestamp = timestamp;
    }
}
