package com.mdaniline.spring.autoproperties;

public class AutoPropertiesFactoryBean {

    private final Class<?> propertiesInterface;

    public AutoPropertiesFactoryBean(Class<?> propertiesInterface) {
        this.propertiesInterface = propertiesInterface;
    }

    public Class<?> getPropertiesInterface() {
        return propertiesInterface;
    }
}
