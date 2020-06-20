package bjad.web;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import bjad.web.properties.EnhancedPropertyHelper;
import bjad.web.provider.ObjectToJSONProvider;
import bjad.web.provider.SSLContextProvider;

/**
 * Factory for web requests being made from
 * properties and property files.
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebRequestFactory
{
   /** Private constructor to prevent instances from being made */
   private BJADWebRequestFactory()
   {
      
   }
   
   /**
    * Creates the BJADWebRequest using the properties within the 
    * EnhancedPropertyHelper passed using the properties defined
    * with the PROPERTY_ prefix within bjad.web.BJADWebConstants.
    * 
    * @param propertyHelper
    *    The property helper to build the request from.
    * @return
    *    The request built using the properties.
    * @throws BJADWebException
    *    Any exceptions loading the properties will be captured
    *    and thrown to the calling function.
    */
   public static BJADWebRequest createRequest(EnhancedPropertyHelper propertyHelper) throws BJADWebException
   {
      BJADWebRequest request = new BJADWebRequest();
      // Gather the URL
      request.setUrl(propertyHelper.getPropertyValue(BJADWebConstants.PROPERTY_URL));
      // Gather the logger name
      request.setLoggerName(propertyHelper.getPropertyValue(BJADWebConstants.PROPERTY_LOGGER_NAME));
      // Gather the connection timeout
      request.setConnectionTimeout(propertyHelper.getNumericValue(BJADWebConstants.PROPERTY_CONNECTION_TIMEOUT, new BigDecimal("15000")).intValue());
      // Gather the read timeout
      request.setReadTimeout(propertyHelper.getNumericValue(BJADWebConstants.PROPERTY_READ_TIMEOUT, new BigDecimal("15000")).intValue());
      // Set the response type character set if found.
      request.setCharacterSetForResponse(propertyHelper.getPropertyValue(BJADWebConstants.PROPERTY_CHARACTER_SET, ""));
      // Set the HTTP Method
      request = populateHttpMethod(request, propertyHelper);
      // Build the headers. 
      request = populateHeaderValues(request, propertyHelper);
      // Build the SSL Context if the provider is defined
      request = populateSSLProvider(request, propertyHelper.getPropertyValue(BJADWebConstants.PROPERTY_SSLCONTEXT_PROVIDER));
      // Build the Object to JSON mapper if the provider is defined
      request = populateObjectToJSONProvider(request, propertyHelper.getPropertyValue(BJADWebConstants.PROPERTY_OBJECT_TO_JSON_PROVIDER));
      
      return request;
   }
   
   /**
    * Builds the HTTP Method type property for the request if it matches 
    * the name of one of the HTTPMethodType enum options.
    * 
    * @param request
    *    The request being built.
    * @param propHelper
    *    The property helper containing the property to use
    * @return
    *    The request object with the HTTPMethodType set if found.
    */
   private static BJADWebRequest populateHttpMethod(BJADWebRequest request, EnhancedPropertyHelper propHelper)
   {
      String val = propHelper.getPropertyValue(BJADWebConstants.PROPERTY_METHOD);
      if (val != null && !val.trim().isEmpty())
      {
         try
         {         
            request.setMethod(HTTPMethodType.valueOf(val.toUpperCase()));
         }
         catch (Exception ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Could not convert \"" + val + "\" into a HTTPMethodType.");
         }
      }
      return request;
   }
   
   /**
    * Builds the header values for the request from the 
    * Header.[headerName] properties in the collection.
    * 
    * @param request
    *    The request being built.
    * @param propHelper
    *    The property helper containing the properties to use
    * @return
    *    The request object with the headers set within it.
    */
   private static BJADWebRequest populateHeaderValues(BJADWebRequest request, EnhancedPropertyHelper propHelper)
   {
      String prefix = BJADWebConstants.PROPERTY_HEADER_PREFIX;
      List<String> keys = propHelper.getPropertyKeys();
      for (String key : keys)
      {
         if (key.startsWith(prefix))
         {
            if (key.length() > prefix.length())
            {
               // Initialize the header list if its currently null.
               if (request.getHeaders() == null)
               {
                  request.setHeaders(new LinkedHashMap<>());
               }               
               String header = key.substring(prefix.length());
               // Add the header value to the request.
               request.getHeaders().put(header.trim(), propHelper.getPropertyValue(key).trim());
            }
         }
      }
      return request;
   }
   
   /**
    * Builds the SSLContext property for the request if the 
    * provider class is defined within the properties.
    * 
    * @param request
    *    The request being built.
    * @param propHelper
    *    The property helper containing the property to use
    * @return
    *    The request object with the SSLContext set within it 
    *    if the provider class was set and could be instantiated.
    */
   private static BJADWebRequest populateSSLProvider(BJADWebRequest request, String className)
   {
      if (className != null && !className.trim().isEmpty())
      {
         try
         {
            Object o = Class.forName(className).newInstance();
            request.setSslContext(((SSLContextProvider)o).getSSLContext());
         }
         catch (ClassCastException ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Class " + className + " is not a SSLContextProvider");
         }
         catch (ClassNotFoundException ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Cannot find " + className + " in classpath.");
         }
         catch (Exception ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Could not create the SSL Context from " + className, ex);
         }
      }
      return request;
   }
   
   /**
    * Builds the Object to JSON property for the request if the 
    * provider class is defined within the properties.
    * 
    * @param request
    *    The request being built.
    * @param propHelper
    *    The property helper containing the property to use
    * @return
    *    The request object with the Object To JSON mapper set within it 
    *    if the provider class was set and could be instantiated.
    */
   private static BJADWebRequest populateObjectToJSONProvider(BJADWebRequest request, String className)
   {
      if (className != null && !className.trim().isEmpty())
      {
         try
         {            
            Object o = Class.forName(className).newInstance();
            request.setGsonImplementation(((ObjectToJSONProvider)o).getObjectToJSONMapper()); 
         }
         catch (ClassCastException ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Class " + className + " is not a ObjectToJSONProvider");
         }
         catch (ClassNotFoundException ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Cannot find " + className + " in classpath.");
         }
         catch (Exception ex)
         {
            LogFactory.getLog(request.getLoggerName()).warn("Could not create the Object To JSON mapper from " + className, ex);
         }
      }
      return request;
   }
}
