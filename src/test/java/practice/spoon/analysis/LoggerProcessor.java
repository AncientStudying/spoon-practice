package practice.spoon.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class LoggerProcessor  extends AbstractProcessor<CtField<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerProcessor.class);

    public final List<CtField<?>> loggers = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtField ctField) {
        LOGGER.trace("ctField value: {}",ctField.getReference().getType().toString());
        TypeFactory typeFactory = new TypeFactory();
        CtTypeReference<Logger> typeReference = typeFactory.createReference(Logger.class);
        LOGGER.trace("typeReference value: {}", typeReference.toString());
        LOGGER.trace("Comparison: {}",ctField.getReference().getType().equals(typeReference));
        return ctField.getReference().getType().equals(typeReference);
    }

    @Override
    public void process(CtField loggerCtField) {
        LOGGER.trace("Processing: {}", loggerCtField.toString());
        loggers.add(loggerCtField);
        LOGGER.trace("Logger List Size: {}", loggers.size());
    }

}

