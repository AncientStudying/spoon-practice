package practice.spoon.transform.loggernormalizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;

import static org.junit.jupiter.api.Assertions.*;

public class RefactoringLoggersTest {

    public static final String DESIRED_NAME = "LOGGER";
    LoggerNormalizer normalizer;

    @BeforeEach
    void beforeEach() {
        normalizer = LoggerNormalizer.factory();
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .readClasses()
            .findLoggers();
    }

    @Test
    void testLoggersHaveValidNames() {
        CtField<?> logger = normalizer.getLoggerProcessor().loggers.get(0);
        assertNotEquals(DESIRED_NAME,logger.getSimpleName());
        normalizer.refactorLoggers();
        assertEquals(DESIRED_NAME,logger.getSimpleName());
    }

    @Test
    void testLoggersHaveValidModifers() {
        CtField<?> logger = normalizer.getLoggerProcessor().loggers.get(0);
        assertEquals(0,logger.getModifiers().size());
        normalizer.refactorLoggers();
        assertEquals(3,logger.getModifiers().size());
        assertTrue(logger.getModifiers().contains(ModifierKind.PRIVATE));
        assertTrue(logger.getModifiers().contains(ModifierKind.STATIC));
        assertTrue(logger.getModifiers().contains(ModifierKind.FINAL));
    }

}
