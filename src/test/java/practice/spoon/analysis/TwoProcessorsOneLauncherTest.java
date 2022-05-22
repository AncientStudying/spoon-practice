package practice.spoon.analysis;

import com.mycompany.myapp.service.MailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TwoProcessorsOneLauncherTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwoProcessorsOneLauncherTest.class);
    private static final String[] ARGS = {
        "-i", "src/main/java/",
        "-o", "target/spooned/"
    };
    public static final int EXPECTED_LOGGER_CNT = 12;
    public static final int EXPECTED_MSERV_CNT = 2;

    private static LoggerProcessor loggerProcessor;
    private static MailServiceProcessor mailServiceProcessor;


    @BeforeAll
    static void beforeAll() {
        LOGGER.debug("beforeAll() fired");
        Launcher launcher = new Launcher();
        launcher.setArgs(ARGS);
        launcher.run();

        Factory factory = launcher.getFactory();
        ProcessingManager processingManager = new QueueProcessingManager(factory);
        loggerProcessor = new LoggerProcessor();
        mailServiceProcessor = new MailServiceProcessor();
        processingManager.addProcessor(loggerProcessor);
        processingManager.addProcessor(mailServiceProcessor);
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

    @Test
    public void allMailServicesFound() {
        LOGGER.trace("MailService List Size:{}", mailServiceProcessor.mailServices.size());
        assertEquals(EXPECTED_MSERV_CNT, mailServiceProcessor.mailServices.size(), "Incorrect number of MailServices found");
    }

    @Test
    public void allFoundAreMailServicesLoggers() {
        mailServiceProcessor.mailServices.forEach(service -> assertEquals(MailService.class.getCanonicalName(), service.getReference().getType().getQualifiedName(),"Not a MailService"));
    }

}
