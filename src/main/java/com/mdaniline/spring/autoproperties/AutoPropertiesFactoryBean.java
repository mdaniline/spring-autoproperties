package com.mdaniline.spring.autoproperties;

import com.google.common.base.Optional;
import com.google.common.reflect.AbstractInvocationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AutoPropertiesFactoryBean extends AbstractFactoryBean<Object> {

    private static final Log LOG = LogFactory.getLog(AutoPropertiesFactoryBean.class);

    private final Class<?> propertiesInterface;
    private final ClassLoader classLoader;

    public AutoPropertiesFactoryBean(Class<?> propertiesInterface, ClassLoader classLoader) {
        this.propertiesInterface = propertiesInterface;
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> getObjectType() {
        return propertiesInterface;
    }

    @Override
    protected Object createInstance() throws Exception {
        return Proxy.newProxyInstance(classLoader, new Class[]{propertiesInterface},
                new AutoPropertiesInvocationHandler());
    }

    private class AutoPropertiesInvocationHandler extends AbstractInvocationHandler {
        private final ConcurrentMap<Method, Optional<Object>> cache = new ConcurrentHashMap<Method, Optional<Object>>();

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
            Optional<Object> valueWrapper;
            if (!cache.containsKey(method)) {
                valueWrapper = Optional.fromNullable(resolveValue(method));
                cache.putIfAbsent(method, valueWrapper);
            } else {
                valueWrapper = cache.get(method);
            }
            return valueWrapper.orNull();
        }

        private Object resolveValue(Method method) {
            Value val = AnnotationUtils.findAnnotation(method, Value.class);
            if (val != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Resolving @Value annotation on %s with key %s",
                            method.getName(), val.value()));
                }
                ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory) getBeanFactory();
                String strValue = beanFactory.resolveEmbeddedValue(val.value());
                BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();

                Object unconvertedResult = resolver.evaluate(strValue, new BeanExpressionContext(beanFactory, null));
                TypeConverter converter = beanFactory.getTypeConverter();
                return converter.convertIfNecessary(unconvertedResult, method.getReturnType());
            }
            String message = "Method %s on interface %s must be annotated with @Value for auto-properties to work!";
            throw new AutoPropertiesException(String.format(message, method.getName(), method.getDeclaringClass().getName()));
        }
    }
}
