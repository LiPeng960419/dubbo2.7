package com.lipeng.providerdemo.config;

import java.util.Map;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @Author: lipeng
 * @Date: 2021/03/04 18:23
 */
@Configuration
public class ServiceParameterBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements PriorityOrdered {

    @Override
    public int getOrder() {
        return PriorityOrdered.LOWEST_PRECEDENCE;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean instanceof ServiceBean) {
            PropertyValue propertyValue = pvs.getPropertyValue("parameters");
            ConversionService conversionService = getConversionService();

            if (propertyValue != null && propertyValue.getValue() != null && conversionService.canConvert(propertyValue.getValue().getClass(), Map.class)) {
                Map parameters = conversionService.convert(propertyValue.getValue(), Map.class);
                propertyValue.setConvertedValue(parameters);
            }
        }
        return pvs;
    }

    private ConversionService getConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringArrayToStringConverter());
        conversionService.addConverter(new StringArrayToMapConverter());
        return conversionService;
    }

    public static class StringArrayToStringConverter implements Converter<String[], String> {

        @Override
        public String convert(String[] source) {
            return ObjectUtils.isEmpty(source) ? null : StringUtils.arrayToCommaDelimitedString(source);
        }

    }

    public static class StringArrayToMapConverter implements Converter<String[], Map<String, String>> {

        @Override
        public Map<String, String> convert(String[] source) {
            return ObjectUtils.isEmpty(source) ? null : CollectionUtils.toStringMap(source);
        }

    }


}