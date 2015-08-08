package com.mdaniline.spring.autoproperties;

import com.mdaniline.spring.autoproperties.testclasses.ValidProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ValidPropertiesTest.TestConfiguration.class)
@TestPropertySource(properties = "test.property=Resolved Test Property")
public class ValidPropertiesTest {

    @Autowired
    private ValidProperties underTest;

    @Test
    public void getHardCodedString_returnsExpectedHardCodedValue() {
        assertEquals("Hard-coded String", underTest.getHardCodedString());
    }

    @Test
    public void getApplicationProperty_returnsResolvedValue() {
        assertEquals("Resolved Test Property", underTest.getApplicationProperty());
    }

    @Test
    public void getImplicitlyConvertedValue_returnsCorrectValueOfCorrectType() {
        assertEquals(Integer.valueOf(1), underTest.getImplicitlyConvertedInteger());
    }

    @Test
    public void getSpelStringValue_returnsResultOfSpelFunction() {
        assertEquals("Hello World", underTest.getSpelString());
    }

    @Test
    public void getSpelArrayValue_returnsResultOfSpelFunction() {
        assertThat(asList(underTest.getSpelArray()), contains("Hello", "World"));
    }

    @Test
    public void getCombinedValue_returnsSpelFunctionEvaluatedWithResolvedApplicationProperty() {
        assertThat(asList(underTest.getApplicationSpelComboProperty()),contains("Resolved", "Test", "Property"));
    }

    @Configuration
    @EnableAutoProperties(basePackageClasses = ValidProperties.class)
    public static class TestConfiguration {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}
