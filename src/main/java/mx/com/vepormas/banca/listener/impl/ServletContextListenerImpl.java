package mx.com.vepormas.banca.listener.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.util.ResourceUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import mx.com.vepormas.lib.resources.ApplicationProperties;

import mx.com.vepormas.lib.exception.InitializationException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.AbstractFileConfiguration;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServletContextListenerImpl implements ServletContextListener {
	
	static Logger logger = LogManager.getLogger(ServletContextListenerImpl.class);
		
	private ApplicationProperties appProps;
	
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext sc = sce.getServletContext();
		
		try {
			
			logger.info( "Starting " + sc.getServletContextName() + " context initialization... " );
			
			//Application configuration resource URL
			String appConfigURL = sc.getInitParameter( "appConfigURL" );
			
			if (StringUtils.isNotBlank( appConfigURL )) {
				
				logger.info( "Looking for application configuration resource at: " + appConfigURL );
				
				if (ResourceUtils.isUrl( appConfigURL )) {
					
					//Loads application configuration resource
					AbstractFileConfiguration config = new PropertiesConfiguration( 
							ResourceUtils.getURL( appConfigURL ) 
					);
					
					logger.info( 
							"Application configuration successfully loaded, initializing resources..." 
					);
					
					//Check if logging framework should be configured
					if (config.getBoolean( "application.context.logging.init" )) {
						
						//Application logging configuration resource URL
						String loggingConfigURL = config.getString( 
								"application.context.logging.resource.url" 
						);
						
						if (StringUtils.isNotBlank( loggingConfigURL )) {
							
							logger.info( "Looking for logging configuration resource at: " + loggingConfigURL );
							
							if (ResourceUtils.isUrl( loggingConfigURL )) {
								
								//Loads logging configuration
								//DOMConfigurator.configure( ResourceUtils.getURL( loggingConfigURL ) );
								
								XmlConfiguration configuration = new XmlConfiguration(null,
							    ConfigurationSource.fromUri(ResourceUtils.getURL( loggingConfigURL ).toURI()));
								configuration.initialize();
								
								logger.info( "Logging framework successfully configured" );
								
							} else {
								
								throw new InitializationException( 
										"Invalid logging configuration resource URL: " + loggingConfigURL 
								);
								
							}
							
						} else {
							
							throw new InitializationException(  
									"Unable to get logging configuration resource URL from configuration" 
							);
							
						}
						
					} else {
						logger.info( "Logging framework initialization disabled" );
					}
					
					//Application properties IoC context name
					String appPropsCtxtName = config.getString( 
							"application.context.properties.ioc.name" 
					);
					
					//Application resource(s) URLs
					String[] resourceURLs = config.getStringArray( 
							"application.context.properties.resource.url" 
					);
					
					if (StringUtils.isBlank( appPropsCtxtName )) {
						
						throw new InitializationException(  
								"Unable to get application properties context name from configuration" 
						);
						
					} else if (ArrayUtils.isEmpty( resourceURLs )) {
						
						throw new InitializationException( 
								"Unable to get application resource(s) URL(s) from configuration" 
						);
						
					} else {
						
						logger.info( "Looking for application properties instance into IoC context..." );
						
						//Creates IoC context instance
						WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext( sc );
						
						//Application properties IoC context lookup
						appProps = (ApplicationProperties) wac.getBean( 
								appPropsCtxtName, 
								ApplicationProperties.class 
						);
						
						if (appProps != null) {
							
							logger.info( "Application properties successfully instantiated, looking for resource(s)..." );
							
							//Loads application configuration into application properties
							appProps.addConfiguration( 
									config, 
									config.getString( "application.resource.name" ) 
							);
							
							logger.info( 
									resourceURLs.length 
									+ " application resource(s) found, loading into application ..." 
							);
							
							//Loads resource(s) into application properties
							for (String resourceURL : resourceURLs) {
								
								logger.info( "Looking for application resource at: " + resourceURL );
								
								if (ResourceUtils.isUrl( resourceURL )) {
									
									//Creates a resource instance
									AbstractFileConfiguration pc = new PropertiesConfiguration( 
											ResourceUtils.getURL( resourceURL )
									);
									
									//Loads resource instance into application properties
									appProps.addConfiguration( 
											pc, 
											pc.getString( "application.resource.name" ) 
									);
									
									StrBuilder sb = new StrBuilder( "Application resource:" );
									sb.appendSeparator( " " );
									sb.append( pc.getString( "application.resource.title" ) );
									sb.appendSeparator( " " );
									sb.append( "successfully loaded" );
									
									logger.info( sb.toString() );
									
								} else {
									
									throw new InitializationException( 
											"Invalid application resource URL: " + resourceURL 
									);
									
								}
								
							}
							
							logger.info( "Application resource(s) successfully loaded, initializing other component(s)..." );
							
							//TODO: initialize other components with an interface contract throughout spring context
							
							logger.info( sc.getServletContextName() + " context successfully initialized" );
							
						} else {
							
							throw new InitializationException( 
									"Unable to get application properties intance from IoC context" 
							);
							
						}
						
					}
					
				} else {
					
					throw new InitializationException( 
							"Invalid application configuration resource URL: " + appConfigURL 
					);
					
				}
		
			} else {
				
				throw new InitializationException(  
						"Unable to get application configuration resource URL from web context" 
				);
				
			}
			
		} catch (Exception e) {
			
			logger.error( sc.getServletContextName() + " context initialization failed", e );
			
			throw new Error( e );
			
		}
		
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		
		ServletContext sc = sce.getServletContext();
		
		try {
			
			//TODO: stop and destroy any component with an interface contract throughout spring context
			
			logger.info( sc.getServletContextName() + " context successfully destroyed" );
			
		} catch (Exception e) {
			
			logger.error( sc.getServletContextName() + " context destruction failed", e );
			
			throw new Error( e );
			
		}
		
	}

}
