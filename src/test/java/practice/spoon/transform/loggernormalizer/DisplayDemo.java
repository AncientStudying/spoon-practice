package practice.spoon.transform.loggernormalizer;

public class DisplayDemo {
    public static void main(String[] args) {
        LoggerNormalizer normalizer = LoggerNormalizer.factory()
            .addSources("src/test/java/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
//            .addSources("target/loggernormalizer/practice/spoon/transform/loggernormalizer/OffendingSourceCode.java")
            .readClasses()
            .display();
    }

}
