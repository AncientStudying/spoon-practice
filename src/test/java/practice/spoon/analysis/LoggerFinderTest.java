package practice.spoon.analysis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.declaration.CtField;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LoggerFinderTest extends AbstractProcessor<CtField<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFinderTest.class);

    public final List<CtField<?>> loggers = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtField<?> ctField) {
        LOGGER.debug(ctField.getReference().getType().getQualifiedName());
        return ctField.getReference().getType().getQualifiedName().equals(Logger.class.getCanonicalName());

    }

    @Override
    public void process(CtField<?> loggerCtField) {
        loggers.add(loggerCtField);
    }

    @Test
    public void testLoggerFinder() {
        final String[] args = {
            "-i", "src/main/java/",
            "-o", "target/spooned/"
        };

        final Launcher launcher = new Launcher();
        launcher.setArgs(args);
        launcher.run();

        final Factory factory = launcher.getFactory();
        final ProcessingManager processingManager = new QueueProcessingManager(factory);
        final LoggerFinderTest processor = new LoggerFinderTest();
        processingManager.addProcessor(processor);
        processingManager.process(factory.Class().getAll());

        processor.loggers.forEach(loggerCtField -> assertEquals("Not a logger", Logger.class.getCanonicalName(), loggerCtField.getReference().getType().getQualifiedName()));

    }

}
