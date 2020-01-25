package me.johnniang.ncov.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.ncov.config.properties.NotificationProperties;
import me.johnniang.ncov.model.TimeLine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static me.johnniang.ncov.model.NcovConst.LATEST_TIMESTAMP_PATH;

/**
 * 2019-nCoV scan job.
 * @author LeiXinXin
 * @author johnniang
 */
@Component
@Slf4j
public class NcovScanJob {

    public static final String TIMELINE_PREFIX = "try { window.getTimelineService = ";

    public static final String TIMELINE_SUFFIX = "}catch(e){}";

    private final NotificationProperties notification;

    private final ObjectMapper objectMapper;

    private final ApplicationEventPublisher eventPublisher;

    public NcovScanJob(NotificationProperties notification,
        ObjectMapper mapper,
        ApplicationEventPublisher publisher) {
        this.notification = notification;
        objectMapper = mapper;
        eventPublisher = publisher;
    }

    @Scheduled(cron = "${ncov.cron:0/30 * * * * ?}")
    public void scan() throws IOException {
        log.info("Starting scanning latest messages from dxy");

        // Get the latest timeline from remote
        List<TimeLine> timeLines = this.fetchLatestTimeLines();

        if (timeLines.size() == 0) {
            log.info("No timeline available!");
            return;
        }

        // Sort the timeline(s)
        timeLines.sort(new TimeLine());

        long latestTimestamp = timeLines.get(0).getPubDate().getTime();

        // Get the latest timestamp
        long lastTimestamp = this.getLatestTimestamp(latestTimestamp - 1);

        // Check the latest message
        List<TimeLine> latestTimelines = new LinkedList<>();
        for (TimeLine timeline : timeLines) {
            if (timeline.getPubDate().getTime() <= lastTimestamp) {
                break;
            }
            latestTimelines.add(timeline);
        }

        if (latestTimelines.size() == 0) {
            log.info("No updated messages");
            return;
        }

        log.info("Found [{}] timeline(s)", latestTimelines.size());

        // Write timestamp into file
        Files.write(LATEST_TIMESTAMP_PATH, String.valueOf(latestTimestamp).getBytes());

        // Send timeline(s) to email
        log.info("Sending to user(s)");

        // Send to users
        this.eventPublisher.publishEvent(new UpdatedMessageEvent(this, latestTimelines, lastTimestamp));
    }

    private long getLatestTimestamp(long defaultTimestamp) throws IOException {

        // Create file if it doesn't exist
        if (Files.notExists(LATEST_TIMESTAMP_PATH)) {
            Files.createFile(LATEST_TIMESTAMP_PATH);
            return defaultTimestamp;
        }

        // Read the latest timestamp
        String timestampString = new String(Files.readAllBytes(LATEST_TIMESTAMP_PATH), StandardCharsets.UTF_8);

        long timestamp = 0;
        if (timestampString.isEmpty()) {
            // Create the file for the first time
            return defaultTimestamp;
        }

        // Convert timestamp string to timestamp
        return Long.parseLong(timestampString);
    }

    private List<TimeLine> fetchLatestTimeLines() throws IOException {
        Document document = Jsoup.connect("https://3g.dxy.cn/newh5/view/pneumonia")
            .timeout(30000)
            .header("cache-control", "no-cache")
            .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
            .get();

        // Get the timeline data (json string)
        Element timelineService = document.getElementById("getTimelineService");

        if (log.isDebugEnabled()) {
            log.debug("Timeline service script content: [{}]", timelineService);
        }

        if (timelineService.childNodeSize() != 1) {
            throw new IllegalStateException("Script format of getTimelineService was invalid");
        }

        Node node = timelineService.childNodes().get(0);

        String timeLineString = node.toString();

        String timeLineJsonString = timeLineString.substring(TIMELINE_PREFIX.length(), timeLineString.length() - TIMELINE_SUFFIX.length());

        TimeLine[] timeLine = objectMapper.readValue(timeLineJsonString, TimeLine[].class);

        log.debug("Got timeline(s): {}", timeLine);

        return Arrays.asList(timeLine);
    }

}