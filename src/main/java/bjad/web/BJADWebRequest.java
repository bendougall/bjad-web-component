package bjad.web;

import java.util.Map;

import javax.net.ssl.SSLContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Request object containing all the information 
 * needed by the BjadWebComponent to gather data
 * from a web resource.
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebRequest
{
   /**
    * The name of the logger to use for the 
    * operation.
    */
   protected String loggerName = "bjad.web.BJADWebComponent";
   
   /** 
    * The URL to call for the operation. 
    */
   protected String url;
   
   /**
    * The HTTP headers for the operation.
    */
   protected Map<String, String> headers;
   
   /**
    * The HTTP method to use for the operation
    */
   protected HTTPMethodType method;
   
   /**
    * The number of milliseconds to wait to establish
    * the connection to the HTTP server.
    */
   protected int connectionTimeout = 15000;
   
   /**
    * The number of milliseconds to wait while the 
    * HTTP operation completes.
    */
   protected int readTimeout = 15000;
   
   /**
    * The SSL context to use for the HTTP
    * operation.
    */
   protected SSLContext sslContext = null;

   /**
    * The GSON implementation to use within the component.
    * By default, the implementation will be one that
    * disables the HTML escaping feature within JSON.
    */
   protected Gson gsonImplementation = new GsonBuilder().disableHtmlEscaping().create();
   
   /**
    * The character set to use to build the string from the 
    * response data from the HTTP server (unless a byte array
    * is the requested return type). Default will be no character
    * set defined so it will be up to Java's default for strings.
    */
   protected String characterSetForResponse = null;
   
   /**
    * @return 
    *   The loggerName property within the WebOperationRequest instance
    */
   public String getLoggerName()
   {
      return this.loggerName;
   }

   /**
    * @param loggerName 
    *   The loggerName to set within the WebOperationRequest instance
    */
   public void setLoggerName(String loggerName)
   {
      if (!(loggerName == null || loggerName.trim().isEmpty()))
      {
         this.loggerName = loggerName;
      }      
   }

   /**
    * @return 
    *   The url property within the WebOperationRequest instance
    */
   public String getUrl()
   {
      return this.url;
   }

   /**
    * @param url 
    *   The url to set within the WebOperationRequest instance
    */
   public void setUrl(String url)
   {
      this.url = url;
   }

   /**
    * @return 
    *   The headers property within the WebOperationRequest instance
    */
   public Map<String, String> getHeaders()
   {
      return this.headers;
   }

   /**
    * @param headers 
    *   The headers to set within the WebOperationRequest instance
    */
   public void setHeaders(Map<String, String> headers)
   {
      this.headers = headers;
   }

   /**
    * @return 
    *   The method property within the WebOperationRequest instance
    */
   public HTTPMethodType getMethod()
   {
      return this.method;
   }

   /**
    * @param method 
    *   The method to set within the WebOperationRequest instance
    */
   public void setMethod(HTTPMethodType method)
   {
      this.method = method;
   }

   /**
    * @return 
    *   The connectionTimeout property within the WebOperationRequest instance
    */
   public int getConnectionTimeout()
   {
      return this.connectionTimeout;
   }

   /**
    * @param connectionTimeout 
    *   The connectionTimeout to set within the WebOperationRequest instance
    */
   public void setConnectionTimeout(int connectionTimeout)
   {
      this.connectionTimeout = connectionTimeout;
   }

   /**
    * @return 
    *   The readTimeout property within the WebOperationRequest instance
    */
   public int getReadTimeout()
   {
      return this.readTimeout;
   }

   /**
    * @param readTimeout 
    *   The readTimeout to set within the WebOperationRequest instance
    */
   public void setReadTimeout(int readTimeout)
   {
      this.readTimeout = readTimeout;
   }

   /**
    * @return 
    *   The sslContext property within the WebOperationRequest instance
    */
   public SSLContext getSslContext()
   {
      return this.sslContext;
   }

   /**
    * @param sslContext 
    *   The sslContext to set within the WebOperationRequest instance
    */
   public void setSslContext(SSLContext sslContext)
   {
      this.sslContext = sslContext;
   }

   /**
    * @return 
    *   The gsonImplementation property within the WebOperationRequest instance
    */
   public Gson getGsonImplementation()
   {
      return this.gsonImplementation;
   }

   /**
    * @param gsonImplementation 
    *   The gsonImplementation to set within the WebOperationRequest instance
    */
   public void setGsonImplementation(Gson gsonImplementation)
   {
      this.gsonImplementation = gsonImplementation;
   }

   /**
    * @return 
    *   The characterSetForResponse property within the WebOperationRequest instance
    */
   public String getCharacterSetForResponse()
   {
      return this.characterSetForResponse;
   }

   /**
    * @param characterSetForResponse 
    *   The characterSetForResponse to set within the WebOperationRequest instance
    */
   public void setCharacterSetForResponse(String characterSetForResponse)
   {
      this.characterSetForResponse = characterSetForResponse;
   }
}
