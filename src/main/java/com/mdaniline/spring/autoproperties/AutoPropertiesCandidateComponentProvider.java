package com.mdaniline.spring.autoproperties;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class AutoPropertiesCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {

    public AutoPropertiesCandidateComponentProvider() {
        super(false);
        addIncludeFilter(new AnnotationTypeFilter(AutoProperties.class, false, true));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}
