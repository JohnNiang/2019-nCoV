package me.johnniang.ncov.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import me.johnniang.ncov.config.properties.NotificationProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.thymeleaf.TemplateEngine;

@RunWith(MockitoJUnitRunner.class)
public class NcovScanJobTest {

    @Mock
    private TemplateEngine engine;

    @Mock
    private NotificationProperties notification;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private NcovScanJob ncovScanJob;

    @Test
    public void mockLoad() {
        Assert.assertNotNull(this.ncovScanJob);
    }

    @Test
    public void scan() throws IOException {
        this.ncovScanJob.scan();
    }
}