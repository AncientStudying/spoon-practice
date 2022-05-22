package practice.spoon.analysis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggerProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerProcessorTest.class);
    private static final String[] ARGS = {
        "-i", "src/main/java/",
        "-o", "target/spooned/"
    };
    public static final int EXPECTED_LOGGER_CNT = 12;

    private static LoggerProcessor loggerProcessor;

    @BeforeAll
    static void beforeAll() {
        LOGGER.debug("beforeAll() fired");
        Launcher launcher = new Launcher();
        launcher.setArgs(ARGS);
        launcher.run();

        Factory factory = launcher.getFactory();
        ProcessingManager processingManager = new QueueProcessingManager(factory);
        loggerProcessor = new LoggerProcessor();
        processingManager.addProcessor(loggerProcessor);
        processingManager.process(factory.Class().getAll());
    }


    @Test
    public void allLoggersFound() {
        LOGGER.trace("Logger List Size:{}", loggerProcessor.loggers.size());
        assertEquals(EXPECTED_LOGGER_CNT, loggerProcessor.loggers.size(), "Incorrect number of loggers found");
    }

    @Test
    public void allFoundAreSlf4jLoggers() {
        loggerProcessor.loggers.forEach(loggerCtField -> assertEquals(Logger.class.getCanonicalName(), loggerCtField.getReference().getType().getQualifiedName(),"Not a SLF4J logger"));
    }

}
