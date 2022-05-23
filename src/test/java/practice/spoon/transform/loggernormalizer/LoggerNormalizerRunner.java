package practice.spoon.transform.loggernormalizer;

/**
 * This class based off of the Spoonerism Presentation.
 * @see <a href="https://github.com/SpoonLabs/spoon-examples/blob/master/docs/spoonerism.fodp">Spoonerism</a>
 * @see <a href="https://github.com/alshopov/spoonerism">GitHub -> alshopov -> spoonerism</a>
 * @see <a href="https://github.com/alshopov/spoonerism/blob/master/doc/700TonsOfCodeLater.odp">700 Tons of Code Later</a>
 */

public class LoggerNormalizerRunner {
    public static void main(String[] args) {

        LoggerNormalizer.factory()
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/")
            .readClasses()
            .findLoggers()
            .refactorLoggers()
            .writeTransformedClasses("target/logger-normalizer/");

    }
}
