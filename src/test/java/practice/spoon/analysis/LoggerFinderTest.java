package practice.spoon.analysis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoggerFinderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFinderTest.class);

    @Test
    public void testLoggerFinder() {
        String[] args = {
            "-i", "src/main/java/",
            "-o", "target/spooned/"
        };

        Launcher launcher = new Launcher();
        launcher.setArgs(args);
        launcher.run();

        Factory factory = launcher.getFactory();
        ProcessingManager processingManager = new QueueProcessingManager(factory);
        LoggerProcessor loggerProcessor = new LoggerProcessor();
        processingManager.addProcessor(loggerProcessor);
        processingManager.process(factory.Class().getAll());

        LOGGER.trace("Logger List Size:{}", loggerProcessor.loggers.size());

        assertTrue("Loggers NOT Found", loggerProcessor.loggers.size() > 0);

        loggerProcessor.loggers.forEach(loggerCtField -> assertEquals("Not a logger", Logger.class.getCanonicalName(), loggerCtField.getReference().getType().getQualifiedName()));

    }

}
