package practice.spoon.transform.loggernormalizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.SpoonException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReadClassesTest {

    LoggerNormalizer normalizer;

    @BeforeEach
    void beforeEach() {
        normalizer = LoggerNormalizer.factory();
    }

    @Test
    void testReadClassesWithClassFile() {
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .readClasses();
    }

    @Test
    void testReadClassesWithTwoClassFile() {
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/LoggerNormalizerRunner.java")
            .readClasses();
    }

    @Test
    void testReadClassesWithSourceDirectory() {
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/")
            .readClasses();
    }

    @Test
    void testReadClassesSecondTimeGeneratesException() {
        // this one should succeed
        normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .readClasses();

        // this one should fail
        SpoonException spoonException = assertThrows(SpoonException.class, () -> normalizer
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/LoggerNormalizerRunner.java")
            .readClasses());

        assertEquals("Model already built", spoonException.getMessage());
    }
}
