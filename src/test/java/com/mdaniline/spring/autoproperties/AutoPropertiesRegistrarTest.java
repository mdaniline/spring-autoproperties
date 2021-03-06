package com.mdaniline.spring.autoproperties;

import com.mdaniline.spring.autoproperties.testclasses.ValidProperties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class AutoPropertiesRegistrarTest {

    private BeanDefinitionRegistry registry;
    private AnnotationMetadata metadata;

    private AutoPropertiesRegistrar underTest;

    @Before
    public void setUp() throws Exception {
        metadata = new StandardAnnotationMetadata(TestConfiguration.class, true);
        registry = new DefaultListableBeanFactory();

        underTest = new AutoPropertiesRegistrar();
    }

    @Test
    public void givenPackageWithAnnotatedInterfaces_registerDefinitions_addsExpectedBeansToRegistryWithExpectedNames() {
        underTest.setResourceLoader(new DefaultResourceLoader());
        underTest.setEnvironment(new StandardEnvironment());
        underTest.registerBeanDefinitions(metadata, registry);

        List<String> beanNames = Arrays.asList(registry.getBeanDefinitionNames());
        assertThat(beanNames, containsInAnyOrder("badlyConfiguredProperties", "validProperties"));
    }

    @EnableAutoProperties(basePackageClasses = ValidProperties.class)
    public static class TestConfiguration {
    }
}