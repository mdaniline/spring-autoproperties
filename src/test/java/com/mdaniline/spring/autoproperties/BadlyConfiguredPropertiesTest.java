package com.mdaniline.spring.autoproperties;

import com.mdaniline.spring.autoproperties.testclasses.BadlyConfiguredProperties;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BadlyConfiguredPropertiesTest.TestConfiguration.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
public class BadlyConfiguredPropertiesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private BadlyConfiguredProperties badlyConfiguredProperties;

    @Test
    public void givenPropertiesWithoutValue_attemptToReadValue_throwsIllegalStateException() throws Exception {
        exception.expect(AutoPropertiesException.class);
        exception.expectMessage("Method getValueWithoutAnnotation on interface " +
                "com.mdaniline.spring.autoproperties.testclasses.BadlyConfiguredProperties " +
                "must be annotated with @Value for auto-properties to work!");
        badlyConfiguredProperties.getValueWithoutAnnotation();
    }

    @Configuration
    @EnableAutoProperties(basePackageClasses = BadlyConfiguredProperties.class)
    public static class TestConfiguration {
    }
}
