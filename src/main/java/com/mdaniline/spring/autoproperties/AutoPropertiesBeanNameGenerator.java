package com.mdaniline.spring.autoproperties;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;

public class AutoPropertiesBeanNameGenerator implements BeanNameGenerator, BeanClassLoaderAware {

    private ClassLoader classLoader;

    private static final BeanNameGenerator DEFAULT_NAME_GENERATOR = new AnnotationBeanNameGenerator();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        AnnotatedBeanDefinition annotatedDefinition = new AnnotatedGenericBeanDefinition(getPropertiesInterface(definition));
        return DEFAULT_NAME_GENERATOR.generateBeanName(annotatedDefinition, registry);
    }

    private Class<?> getPropertiesInterface(BeanDefinition beanDefinition) {
        String value = (String) beanDefinition.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue();
        try {
            return ClassUtils.forName(value, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
