package com.demus;
/*package com.demus;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//@Configuration
//@ConditionalOnClass(Gson.class)
//@ConditionalOnMissingClass(name = "com.fasterxml.jackson.core.JsonGenerator")
//@ConditionalOnBean(Gson.class) 
public class GsonHttpMessageConverterConfiguration {

    @Bean
    //@ConditionalOnMissingBean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
    	JsonSerializer<Date> ser = new JsonSerializer<Date>() {
    		  @Override
    		  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext 
    		             context) {
    		    return src == null ? null : new JsonPrimitive(src.getTime());
    		  }
    		};

    		JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
    		  @Override
    		  public Date deserialize(JsonElement json, Type typeOfT,
    		       JsonDeserializationContext context) throws JsonParseException {
    			  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    				if (json != null) {
    					 String dateInString = json.getAsString();
    					 try {
							Date date = formatter.parse(dateInString);
							return date;
						} catch (ParseException e) {
							return null;
						}
    				}
					return null;
    		  }
    		};
    		GsonBuilder builder = new GsonBuilder();
    	gson = builder.setExclusionStrategies(new AnnotationExclusionStrategy()).registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser).create();
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }

}*/