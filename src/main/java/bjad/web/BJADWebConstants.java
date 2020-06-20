package bjad.web;

/**
 * Constants to use within the BJADWebComponent
 * and its consumers.
 *
 * @author 
 *  Ben Dougall
 */
public final class BJADWebConstants
{  
   /**
    * <p>
    * The property name to look for in order to 
    * load properties from an additional file
    * or classpath resource. Each resource needs  
    * to be defined with a number, so AdditionalPropertyFile.0 
    * for the first additional file, AdditionalPropertyFile.1
    * for the second, etc... 
    * 
    * <p>
    * Each resource can be defined with a case-sensitive prefix 
    * (classpath: or file:) to define how the file will be loaded. 
    * <i>Failure to provide a prefix means the file will not be loaded.</i>
    */
   public static final String PROPERTY_ADDITIONAL_PROPERTY_FILE_PREFIX = "AdditionalPropertyFile.";
   
   /**
    * The property name for the URL to use for the 
    * operation. 
    */
   public static final String PROPERTY_URL = "URL";
   
   /**
    * The property name for the logger to use for the 
    * operation. By default, the web component's classname
    * is used.
    */
   public static final String PROPERTY_LOGGER_NAME = "LoggerName";
   
   /**
    * The property name for the HTTP Method to use for the 
    * operation. The value needs to match one of the 
    * options within the {@link bjad.web.HTTPMethodType} enum.
    */
   public static final String PROPERTY_METHOD = "HTTPMethod";
      
   /**
    * The property name to set for the connection timeout
    * to use (value in milliseconds).
    */
   public static final String PROPERTY_CONNECTION_TIMEOUT = "ConnectionTimeout";
   
   /**
    * The property name to set for the read timeout
    * to use (value in milliseconds).
    */
   public static final String PROPERTY_READ_TIMEOUT = "ReadTimeout";
   
   /**
    * The property name for the character set to 
    * apply to the request. 
    */
   public static final String PROPERTY_CHARACTER_SET = "CharacterSet";
   
   /**
    * The property name to look for in order to 
    * load header values for the upcoming HTTP operation. 
    * Each header needs to be defined by a Header.[key]
    */
   public static final String PROPERTY_HEADER_PREFIX = "Header.";
   
   /**
    * The property for to use in order to specify the 
    * class that implements the bjad.web.provider.SSLContextProvider 
    * interface in order to generate the SSLContext
    * for the operation.
    */
   public static final String PROPERTY_SSLCONTEXT_PROVIDER = "SSLContextProvider";
   
   /**
    * The property for to use in order to specify the 
    * class that implements the bjad.web.provider.ObjectToJSONProvider
    * interface to order to generate the GSON implementation
    * for the operation.
    */
   public static final String PROPERTY_OBJECT_TO_JSON_PROVIDER = "ObjectToJSONProvider";
   
   /**
    * The prefix to use if additional property/
    * properties file needs to be loaded from a 
    * classpath file.
    */
   public static final String ADDITIONAL_FILE_CLASSPATH_PREFIX = "classpath:";
   
   /**
    * The prefix to use if additional property/
    * properties file needs to be loaded from
    * a file within the file system. 
    */
   public static final String ADDITIONAL_FILE_FILE_PREFIX = "file:";
   
   /**
    * Hidden constructor to prevent instances from being created.
    */
   private BJADWebConstants()
   {
   }
}
