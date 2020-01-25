package me.johnniang.ncov.job;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.ncov.config.properties.NotificationProperties;
import me.johnniang.ncov.model.TimeLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static me.johnniang.ncov.model.NcovConst.LATEST_TIMESTAMP_PATH;

/**
 * Updated message listener.
 * @author johnniang
 */
@Component
@Slf4j
public class UpdatedMessageListener {

    private final JavaMailSender mailSender;

    private final NotificationProperties notification;

    private final String username;

    private final TemplateEngine templateEngine;

    public UpdatedMessageListener(JavaMailSender sender,
        NotificationProperties notification,
        @Value("${spring.mail.username}") String username,
        TemplateEngine engine) {
        mailSender = sender;
        this.notification = notification;
        this.username = username;
        templateEngine = engine;
    }

    @Async
    @EventListener
    public void handleUpdatedMessage(UpdatedMessageEvent updatedMessageEvent) throws MessagingException, IOException {
        log.debug("Handling an updated message event");

        List<TimeLine> timelines = updatedMessageEvent.getTimeLines();
        if (timelines == null || timelines.size() == 0) {
            log.info("No updated timelines are contained");
            return;
        }

        Context context = new Context();
        context.setVariable("timelines", timelines);
        String latestRender = templateEngine.process("latest", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(username);
        messageHelper.setTo(notification.getTo());
        messageHelper.setSubject(timelines.get(0).getPubDateStr() + " | 最新疫情消息");
        messageHelper.setText(latestRender, true);

        // Send email
        mailSender.send(messageHelper.getMimeMessage());

        // Write timestamp into file
        Files.write(LATEST_TIMESTAMP_PATH, String.valueOf(updatedMessageEvent.getTimestamp()).getBytes());
    }
}
