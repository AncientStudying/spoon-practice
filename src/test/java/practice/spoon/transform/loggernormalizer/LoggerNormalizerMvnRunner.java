package practice.spoon.transform.loggernormalizer;

public class LoggerNormalizerMvnRunner {
    public static void main(String[] args) {

        LoggerNormalizer.factory("./pom.xml")
            .readClasses()
            .findLoggers()
            .refactorLoggers()
            .writeTransformedClasses("target/logger-normalizer-mvn/");

    }
}
