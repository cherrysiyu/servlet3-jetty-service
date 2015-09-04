package com.cherry.application.logback;
import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
 
 public abstract class LogbackConfigurer {
 
     public static final String XML_FILE_EXTENSION = ".xml";
 
     private static LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
     private static JoranConfigurator configurator = new JoranConfigurator();
 
     public static void initLogging(String location) throws FileNotFoundException {
    	File file =  new File(location);
    	if(file.exists() && file.getName().toLowerCase().endsWith(XML_FILE_EXTENSION)){
    		 // DOMConfigurator.configure(url);
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(file.toURI().toURL());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            lc.start();
    	}else{
    		throw new FileNotFoundException(file.getAbsolutePath());
    	}
     }
 
     /**
      * Shut down logback, properly releasing all file locks.
      * <p>
      * This isn't strictly necessary, but recommended for shutting down logback
      * in a scenario where the host VM stays alive (for example, when shutting
      * down an application in a JEE environment).
      */
     public static void shutdownLogging() {
         lc.stop();
     }
 
     /**
      * Set the specified system property to the current working directory.
      * <p>
      * This can be used e.g. for test environments, for applications that
      * leverage logbackWebConfigurer's "webAppRootKey" support in a web
      * environment.
      * 
      * @param key
      *            system property key to use, as expected in logback
      *            configuration (for example: "demo.root", used as
      *            "${demo.root}/WEB-INF/demo.log")
      * @see org.springframework.web.util.logbackWebConfigurer
      */
     public static void setWorkingDirSystemProperty(String key) {
         System.setProperty(key, new File("").getAbsolutePath());
     }
 
 }