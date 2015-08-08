package com.mdaniline.spring.autoproperties;

import com.mdaniline.spring.autoproperties.testclasses.BadlyConfiguredProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BadlyConfiguredPropertiesTest.TestConfiguration.class)
public class BadlyConfiguredPropertiesTest {

    @Autowired
    private BadlyConfiguredProperties badlyConfiguredProperties;

    @Test(expected = IllegalStateException.class)
    public void givenPropertiesWithoutValue_attemptToReadValue_throwsIllegalStateException() {
        badlyConfiguredProperties.getValueWithoutAnnotation();
    }

    @Configuration
    @EnableAutoProperties(basePackageClasses = BadlyConfiguredProperties.class)
    public static class TestConfiguration {
    }
}
