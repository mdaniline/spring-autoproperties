package com.mdaniline.spring.autoproperties;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AutoPropertiesRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AutoPropertiesCandidateComponentProvider provider = new AutoPropertiesCandidateComponentProvider();
        provider.setEnvironment(environment);
        provider.setResourceLoader(resourceLoader);

        AutoPropertiesBeanNameGenerator nameGenerator = new AutoPropertiesBeanNameGenerator();
        nameGenerator.setBeanClassLoader(resourceLoader.getClassLoader());

        Set<BeanDefinition> candidates = new HashSet<BeanDefinition>();
        for (String basePackage : getBasePackages(annotationMetadata)) {
            Set<BeanDefinition> candidatesForPackage = provider.findCandidateComponents(basePackage);
            candidates.addAll(candidatesForPackage);
        }

        for (BeanDefinition definition : candidates) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AutoPropertiesFactoryBean.class.getName());
            builder.getRawBeanDefinition().setSource(annotationMetadata);
            builder.addConstructorArgValue(definition.getBeanClassName());

            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            String beanName = nameGenerator.generateBeanName(beanDefinition, registry);

            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private Iterable<String> getBasePackages(AnnotationMetadata annotationMetadata) {
        String sourceClass = annotationMetadata.getClassName();
        Map<String, Object> attributesMap = annotationMetadata.getAnnotationAttributes(EnableAutoProperties.class.getName());
        AnnotationAttributes attributes = new AnnotationAttributes(attributesMap);

        String[] basePackages = attributes.getAliasedStringArray("basePackages", EnableAutoProperties.class, sourceClass);
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");

        // If we do not have any overrides, use the package of the annotated class itself
        if (basePackageClasses.length == 0 && basePackages.length == 0) {
            return Collections.singleton(ClassUtils.getPackageName(sourceClass));
        }

        Set<String> packages = new HashSet<String>();
        packages.addAll(Arrays.asList(basePackages));

        for (Class<?> basePackageClass : basePackageClasses) {
            packages.add(ClassUtils.getPackageName(basePackageClass));
        }

        return packages;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
