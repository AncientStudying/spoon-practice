package practice.spoon.analysis;

import com.mycompany.myapp.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class MailServiceProcessor extends AbstractProcessor<CtField<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceProcessor.class);

    public final List<CtField<?>> mailServices = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtField ctField) {
        LOGGER.trace("ctField value: {}",ctField.getReference().getType().toString());
        TypeFactory typeFactory = new TypeFactory();
        CtTypeReference<MailService> typeReference = typeFactory.createReference(MailService.class);
        LOGGER.trace("typeReference value: {}", typeReference.toString());
        LOGGER.trace("Comparison: {}",ctField.getReference().getType().equals(typeReference));
        return ctField.getReference().getType().equals(typeReference);
    }

    @Override
    public void process(CtField loggerCtField) {
        LOGGER.trace("Processing: {}", loggerCtField.toString());
        mailServices.add(loggerCtField);
        LOGGER.trace("Service List Size: {}", mailServices.size());
    }

}

