package practice.spoon.transform.loggernormalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.spoon.analysis.LoggerProcessor;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.compiler.Environment;
import spoon.processing.ProcessingManager;
import spoon.refactoring.CtRenameGenericVariableRefactoring;
import spoon.refactoring.CtRenameLocalVariableRefactoring;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;
import spoon.support.gui.SpoonModelTree;

import java.util.LinkedHashSet;
import java.util.Set;

public class LoggerNormalizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerNormalizer.class);
    public static final String DESIRED_NAME = "LOGGER";

    private final SpoonAPI spooniverse;

    private final LoggerProcessor loggerProcessor;

    private LoggerNormalizer() {
        spooniverse = new Launcher();
        loggerProcessor = new LoggerProcessor();
    }

    /**
     * Factory method to properly initialize class
     * @return LoggerNormalizer to be used by subsequent methods
     */
    public static LoggerNormalizer factory() {
        return new LoggerNormalizer();
    }

    public LoggerProcessor getLoggerProcessor() {
        return loggerProcessor;
    }

    public LoggerNormalizer addSources(String inputSource) {
        // apparently we can call addInputResource more than once
        LOGGER.debug("calling addInputResource with: {}", inputSource);
        spooniverse.addInputResource(inputSource);
        return this;
    }

    public LoggerNormalizer readClasses() {
        // parses the input into an AST
        LOGGER.debug("calling buildModel");
        spooniverse.buildModel();
        return this;
    }

    public LoggerNormalizer display() {
        // displays a helpful UI
        LOGGER.debug("instantiating SpoonModelTree");
        SpoonModelTree tree = new SpoonModelTree(spooniverse.getFactory());
        return this;
    }

    public LoggerNormalizer findLoggers() {
        // finds the loggers using the previously written proccessor
        LOGGER.debug("getting the factory");
        Factory factory = spooniverse.getFactory();
        LOGGER.debug("instantiating the ProcessingManager");
        ProcessingManager processingManager = new QueueProcessingManager(factory);
        LOGGER.debug("adding the loggerProcessor");
        processingManager.addProcessor(loggerProcessor);
        LOGGER.debug("triggering the processing");
        processingManager.process(factory.Class().getAll());
        return this;
    }

    public LoggerNormalizer refactorLoggers() {
        LOGGER.debug("iterating over found loggers");
        loggerProcessor.loggers.forEach(logger -> {
            LOGGER.debug("processing logger with simpleName: {}", logger.getSimpleName());
            if(!logger.getSimpleName().equals(DESIRED_NAME)) {
                LOGGER.debug("modifying simpleName");
                CtRenameGenericVariableRefactoring refactor = new CtRenameGenericVariableRefactoring();
                refactor.setTarget(logger);
                refactor.setNewName(DESIRED_NAME);
                refactor.refactor();
            }

            LOGGER.debug("iterating over found modifiers");
            logger.getModifiers().forEach(modifier -> LOGGER.debug("modifier: {}",modifier));

            LOGGER.debug("setting the new modifiers");
            logger.setModifiers(defaultModifiers());
        });
        return this;
    }

    public LoggerNormalizer writeTransformedClasses(String outputTarget) {
        Environment env = spooniverse.getEnvironment();
        env.setAutoImports(true);
        env.setCommentEnabled(true);

        spooniverse.setSourceOutputDirectory(outputTarget);
        spooniverse.prettyprint();
        return this;
    }

    private static Set<ModifierKind> defaultModifiers() {
        LinkedHashSet<ModifierKind> retVal = new LinkedHashSet<>(3);
        retVal.add(ModifierKind.PRIVATE);
        retVal.add(ModifierKind.STATIC);
        retVal.add(ModifierKind.FINAL);
        return retVal;
    }
}
