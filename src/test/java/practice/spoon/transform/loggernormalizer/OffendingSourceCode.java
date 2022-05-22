package practice.spoon.transform.loggernormalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OffendingSourceCode {
    Logger log = LoggerFactory.getLogger(OffendingSourceCode.class);

    public static void main(String[] args) {
        OffendingSourceCode osc = new OffendingSourceCode();
        osc.log.info("Message");
    }
}
