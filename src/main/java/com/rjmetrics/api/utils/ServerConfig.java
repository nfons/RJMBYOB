package com.rjmetrics.api.utils;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;


public class ServerConfig extends ResourceConfig {

    /**
	* Register JAX-RS application components.
	*/	
	public ServerConfig(){
		register(RequestContextFilter.class);
		packages("com.rjmetrics.api.rest");
		register(JacksonFeature.class);	
		//register(JavaScriptService.class);
	}
}

