package com.mycompany.myapp;

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

public class LoggerFinderTest extends AbstractProcessor<CtField<Logger>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFinderTest.class);

    public final List<CtField<Logger>> loggers = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtField<Logger> ctField) {
        return (ctField.getAssignment() != null)
            && (ctField.getAssignment().getType().isClass())
            && (ctField.getSimpleName().equals(Logger.class.getSimpleName()));
    }

    @Override
    public void process(CtField<Logger> loggerCtField) {
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

        processor.loggers.forEach(loggerCtField -> {
            assertEquals("Not a logger","Logger",loggerCtField.getSimpleName());
            LOGGER.info("loggerCtField {}", loggerCtField);
            LOGGER.info("loggerCtField.getAssignment() {}", loggerCtField.getAssignment());
            LOGGER.info("loggerCtField.getReference() {}", loggerCtField.getReference());
            LOGGER.info("loggerCtField.getPath() {}", loggerCtField.getPath());
        });


    }

}
