package com.mdaniline.spring.autoproperties.testclasses;

import com.mdaniline.spring.autoproperties.AutoProperties;
import org.springframework.beans.factory.annotation.Value;

@AutoProperties
public interface TestBasicProperties {

    @Value("${string.prop}")
    String getStringProp();

    @Value("${integer.prop}")
    String getIntegerProp();
}
