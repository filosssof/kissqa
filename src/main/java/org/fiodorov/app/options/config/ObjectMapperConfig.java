package org.fiodorov.app.options.config;

import java.time.LocalDateTime;
import java.util.List;

import org.fiodorov.app.options.CustomJsonLocalDateTimeDeserializer;
import org.fiodorov.app.options.CustomJsonLocalDateTimeSerializer;
import org.fiodorov.app.options.CustomStringDeserialazer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.annotations.VisibleForTesting;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
@EnableWebMvc
public class ObjectMapperConfig extends WebMvcConfigurerAdapter {

    // that's our own configured ObjectMapper that spring will pick-up
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(converter());
        converters.add(resourceHttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

    @Bean
    public ResourceHttpMessageConverter resourceHttpMessageConverter() {
        return new ResourceHttpMessageConverter();
    }

    /**
     * this one is static to ease testing when ObjectMapper is required
     */
    @VisibleForTesting
    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        GuavaModule guavaModule = new GuavaModule();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomJsonLocalDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new CustomJsonLocalDateTimeSerializer());
        mapper.registerModule(javaTimeModule);
//        mapper.registerModule(new Jdk8Module());

        mapper.registerModule(guavaModule);

        // trims the serialized string values
        SimpleModule stringCustomModule = new SimpleModule();
        stringCustomModule.addDeserializer(String.class, new CustomStringDeserialazer());
        mapper.registerModule(stringCustomModule);

        // do not include NULL fields
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // do not include empty strings
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        return mapper;
    }

}
