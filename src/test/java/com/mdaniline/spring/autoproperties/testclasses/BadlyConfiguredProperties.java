package com.mdaniline.spring.autoproperties.testclasses;

import com.mdaniline.spring.autoproperties.AutoProperties;
import org.springframework.beans.factory.annotation.Value;

@AutoProperties
public interface BadlyConfiguredProperties {
    String getValueWithoutAnnotation();

    @Value("String")
    Integer getValueOfWrongType();
}
