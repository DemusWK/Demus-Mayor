package com.demus;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
public class JacksonConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jacksonMessageConverter.getObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.registerModule(new JodaModule());
        converters.add(jacksonMessageConverter);
    }
    
    private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            return m.getDeclaringClass() == AbstractAuditable.class || super.hasIgnoreMarker(m);
        }
    }
}