package practice.spoon.transform.loggernormalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.spoon.analysis.LoggerProcessor;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.SpoonAPI;
import spoon.compiler.Environment;
import spoon.processing.ProcessingManager;
import spoon.refactoring.CtRenameGenericVariableRefactoring;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;
import spoon.support.gui.SpoonModelTree;
import spoon.support.sniper.SniperJavaPrettyPrinter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class based off of the Spoonerism Presentation.
 * @see <a href="https://github.com/SpoonLabs/spoon-examples/blob/master/docs/spoonerism.fodp">Spoonerism</a>
 * @see <a href="https://github.com/alshopov/spoonerism">GitHub -> alshopov -> spoonerism</a>
 * @see <a href="https://github.com/alshopov/spoonerism/blob/master/doc/700TonsOfCodeLater.odp">700 Tons of Code Later</a>
 */

public class LoggerNormalizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerNormalizer.class);
    public static final String DESIRED_NAME = "LOGGER";

    private final SpoonAPI spooniverse;

    private final LoggerProcessor loggerProcessor;

    private LoggerNormalizer() {
        spooniverse = new Launcher();
        // todo troubleshoot SniperJavaPrettyPrinter
//        Environment env = spooniverse.getEnvironment();
//        env.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(spooniverse.getEnvironment()));
        Environment env = spooniverse.getEnvironment();
        env.setAutoImports(true);
        env.setCommentEnabled(true);

        loggerProcessor = new LoggerProcessor();
    }

    private LoggerNormalizer(String mvnPath) {
        spooniverse = new MavenLauncher(mvnPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        // todo troubleshoot SniperJavaPrettyPrinter
//        Environment env = spooniverse.getEnvironment();
//        env.setPrettyPrinterCreator(() -> new SniperJavaPrettyPrinter(spooniverse.getEnvironment()));
        Environment env = spooniverse.getEnvironment();
        env.setAutoImports(true);
        env.setCommentEnabled(true);

        loggerProcessor = new LoggerProcessor();
    }

    /**
     * Factory method to properly initialize class
     * @return LoggerNormalizer to be used by subsequent methods
     */
    public static LoggerNormalizer factory() {
        return new LoggerNormalizer();
    }

    public static LoggerNormalizer factory(String mvnPath) {
        return new LoggerNormalizer(mvnPath);
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
