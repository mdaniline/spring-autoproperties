package com.mdaniline.spring.autoproperties.testclasses;

import com.mdaniline.spring.autoproperties.AutoProperties;
import org.springframework.beans.factory.annotation.Value;


@AutoProperties
public interface ValidProperties {

    @Value("Hard-coded String")
    String getHardCodedString();

    @Value("1")
    Integer getImplicitlyConvertedInteger();

    @Value("${test.property}")
    String getApplicationProperty();

    @Value("#{'Hello '.concat('World')}")
    String getSpelString();

    @Value("#{'Hello World'.split(' ')}")
    String[] getSpelArray();

    @Value("#{'${test.property}'.split(' ')}")
    String[] getApplicationSpelComboProperty();
}
