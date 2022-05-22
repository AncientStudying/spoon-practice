package practice.spoon.transform.loggernormalizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FindLoggersTest {

    LoggerNormalizer normalizer;

    @BeforeEach
    void beforeEach() {
        normalizer = LoggerNormalizer.factory();
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .readClasses();
    }

    @Test
    void testLoggersFound() {
        normalizer.findLoggers();
        assertNotNull(normalizer.getLoggerProcessor().loggers);
        assertEquals(1,normalizer.getLoggerProcessor().loggers.size());
    }

}
