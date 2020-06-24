package bjad.web;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import bjad.web.body.AbstractBodyModel;
import bjad.web.body.ObjectJSONStringBody;

/**
 * The main component class for the web component.  
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebComponent
{
   /**
    * The request for the operation, contains URL, headers, method type, 
    * and the other configurable items for the request.
    */
   protected BJADWebRequest operationRequest;
   
   /**
    * The logger to use within the component's operations.
    */
   protected Log logger;
   
   /**
    * Creates a component with a blank request. 
    */
   public BJADWebComponent()
   {
      this(null);
   }
   
   /**
    * Creates the component with a constructed request.
    * @param request
    *    The request to start the component with.
    */
   public BJADWebComponent(BJADWebRequest request)
   {
      this.operationRequest = request;
      String loggerName = this.getClass().getCanonicalName();
      if (request != null)
      {
         if (request.getLoggerName() != null && !request.getLoggerName().trim().isEmpty())
         {
            loggerName = request.getLoggerName().trim();
         }
      }
      this.logger = LogFactory.getLog(loggerName);
   }

   /**
    * @param request 
    *   The request to set within the BJADWebComponent instance
    */
   public void setRequest(BJADWebRequest request)
   {
      this.operationRequest = request;
   }
   
   /**
    * Gathers the content from the web server using the URL and 
    * other properties within the request added to the 
    * object. 
    * @param clazz
    *    The data type to return in the response object.
    * @param <T>
    *    The type of object to retrieve.
    * @return
    *    The response object, containing the status code, 
    *    headers, duration, and of course, the data from 
    *    the operation.
    * @throws BJADWebException
    *    Any exceptions while attempt to get data 
    *    from the web resource will be thrown.    
    */
   public <T> BJADWebResponse<T> performWebCall(Class<T> clazz) throws BJADWebException
   {
      return internalPerformWebCall(operationRequest.getCharacterSetForResponse(), clazz, null);
   }
   
   /**
    * Gathers the content from the web server using the URL and 
    * other properties within the request added to the 
    * object. 
    * 
    * @param clazz
    *    The data type to return in the response object.
    * @param body
    *    The body for the request.
    * @param <T>
    *    The type of object to retrieve.
    * @return
    *    The response object, containing the status code, 
    *    headers, duration, and of course, the data from 
    *    the operation.
    * @throws BJADWebException
    *    Any exceptions while attempt to get data 
    *    from the web resource will be thrown.    
    */
   public <T> BJADWebResponse<T> performWebCall(Class<T> clazz, AbstractBodyModel body) throws BJADWebException
   {
      return internalPerformWebCall(operationRequest.getCharacterSetForResponse(), clazz, body);
   }
   
   /**
    * Gathers the content from the web server using the URL and 
    * other properties within the request added to the 
    * object.
    * 
    * @param clazz
    *    The data type to return in the response object.
    * @param body
    *    The body for the request.
    * @param <T>
    *    The type of object to retrieve.
    * @return
    *    The response object, containing the status code, 
    *    headers, duration, and of course, the data from 
    *    the operation.
    * @throws BJADWebException
    *    Any exceptions while attempt to get data 
    *    from the web resource will be thrown.    
    */
   public <T> BJADWebResponse<T> performWebCall(Class<T> clazz, HttpEntity body) throws BJADWebException
   {
      return internalPerformWebCall(operationRequest.getCharacterSetForResponse(), clazz, body);
   }
   
   /**
    * Gathers the content from the web server using the URL and 
    * other properties within the request added to the 
    * object. 
    * 
    * @param charset
    *    The character set name for the string returned by 
    *    http server
    * @param clazz
    *    The data type to return in the response object.
    * @param body
    *    The body for the request.
    * @param <T>
    *    The type of object to retrieve.
    * @return
    *    The response object, containing the status code, 
    *    headers, duration, and of course, the data from 
    *    the operation.
    * @throws BJADWebException
    *    Any exceptions while attempt to get data 
    *    from the web resource will be thrown.    
    */
   @SuppressWarnings("unchecked") // We test the class, so this warning is moot.
   private <T> BJADWebResponse<T> internalPerformWebCall(String charset, Class<T> clazz, Object body) throws BJADWebException
   {
      // If we are trying to get the bytes from the web operation,
      // simply return the result of getByteContent, but log as
      // we cannot log the output. 
      if (clazz.equals(byte[].class))
      {
         BJADWebResponse<byte[]> bytes = (BJADWebResponse<byte[]>)getByteContent(body);
         logSendorRecv("RECV", operationRequest.getUrl(), operationRequest.getMethod(), operationRequest.getHeaders(),
               (bytes.getData() == null || bytes.getData().length == 0 ? "n/a" : bytes.getData().length + " bytes."));
         return (BJADWebResponse<T>)bytes;
      }
      
      // Not getting the byte content explictly, so get the content
      // but don't log the byte count returned.
      BJADWebResponse<byte[]> bytes = getByteContent(body);
      String content = "";
      try
      {
         // Get the string content from the bytes returned.
         content = charset == null || charset.trim().isEmpty() ? 
            new String(bytes.getData()) :
            new String(bytes.getData(), charset);
      }
      catch (UnsupportedEncodingException ex)
      {
         throw new BJADWebException(ex);
      }
         
      logSendorRecv("RECV", operationRequest.getUrl(), operationRequest.getMethod(), operationRequest.getHeaders(), content);
      
      // If looking for the string, just return it explictly in 
      // a response object 
      if (clazz.equals(String.class))
      {           
         BJADWebResponse<String> returnVal = new BJADWebResponse<>();
         returnVal.copyNonDataValues(bytes);
         returnVal.setData(content);
         
         return (BJADWebResponse<T>)returnVal;
      }
      // Anything else, convert the string to the object using the 
      // requests GSON implementation, and return it.
      else
      {
         BJADWebResponse<T> returnVal = new BJADWebResponse<>();
         returnVal.copyNonDataValues(bytes);
         try
         {
            returnVal.setData(operationRequest.getJsonObjectMapper().readValue(content, clazz));
         }
         catch (JsonProcessingException ex)
         {
            throw new BJADWebException("Failed to convert data include JSON", ex);
         }
         
         return (BJADWebResponse<T>)returnVal;
      }
   }
   
   private BJADWebResponse<byte[]> getByteContent(Object body) throws BJADWebException
   {
      // Build out the HTTP client
      try (final CloseableHttpClient HTTPCLIENT = initializeClientBuilder(operationRequest).build())
      {
         // Create the request base (GET, POST, etc...)
         HttpRequestBase httpRequest = getRequestBaseForMethod(operationRequest);
         
         // Apply the headers to the request.
         httpRequest = applyHTTPHeadersToRequest(httpRequest, operationRequest);
         
         // Apply any body information to the request if applicable
         httpRequest = applyBodyToRequest(httpRequest, operationRequest, body);
      
         long duration = System.currentTimeMillis();
         // Return the byte array gathered by the http client.
         try (final CloseableHttpResponse RESPONSE = HTTPCLIENT.execute(httpRequest))
         {
            duration = System.currentTimeMillis() - duration;
            
            HttpEntity entity = RESPONSE.getEntity();            
            byte[] returnedData = new byte[0];
            
            if (entity != null)
            {
               returnedData = EntityUtils.toByteArray(entity);              
            }
                        
            BJADWebResponse<byte[]> fullResponse = new BJADWebResponse<>();
            fullResponse.setDuration(duration);
            fullResponse.setData(returnedData);
            fullResponse.setStatusCode(RESPONSE.getStatusLine().getStatusCode());
            fullResponse.setHeaders(new LinkedHashMap<>());
            
            Header[] headers = RESPONSE.getAllHeaders();
            for (Header header : headers)
            {
               fullResponse.getHeaders().put(header.getName(), header.getValue());
            }
            return fullResponse;
         }
      }
      catch (Exception ex)
      {
         throw new BJADWebException(ex);
      }
   }

   private HttpClientBuilder initializeClientBuilder(BJADWebRequest request) throws Exception
   {
      int connectTimeout = request.getConnectionTimeout() > -1 ? request.getConnectionTimeout() : 15000;
      int connectReqTimeout = request.getConnectionTimeout() > -1 ? request.getConnectionTimeout() : 15000;
      int readTimeout = request.getReadTimeout() > -1 ? request.getReadTimeout()  : 15000;
      RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(connectReqTimeout)
            .setSocketTimeout(readTimeout > -1 ? readTimeout : 15000)
            .build();
      
     HttpClientBuilder clientBuilder = HttpClients.custom().setDefaultRequestConfig(config);
     if (request.getUrl().toLowerCase().startsWith("https:"))
     {
        SSLContext sslContext = request.getSslContext();
        if (sslContext == null)
        {
           sslContext = SSLContext.getDefault();
        }
        
        clientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier()));
     }
     
     return clientBuilder;
   }
   
   private HttpRequestBase getRequestBaseForMethod(BJADWebRequest request)
   {
      HttpRequestBase obj = null;
      switch (request.getMethod())
      {
      case DELETE:
         obj = new HttpDelete(request.getUrl());
         break;
      case GET:
         obj = new HttpGet(request.getUrl());
         break;
      case HEAD:
         obj = new HttpHead(request.getUrl());
         break;
      case OPTIONS:
         obj = new HttpOptions(request.getUrl());
         break;
      case PATCH:
         obj = new HttpPatch(request.getUrl());
         break;
      case POST:
         obj = new HttpPost(request.getUrl());
         break;
      case PUT:
         obj = new HttpPut(request.getUrl());
         break;
      default:
         obj = new HttpTrace(request.getUrl());
         break;
      }
      return obj;
   }
      
   /**
    * Applies the headers from the operation request to the 
    * HTTP request being built.
    * 
    * @param httpRequest
    *    The http request being built.
    * @param opRequest
    *    The operation request, containing the headers to add.
    * @return
    *    The http request being built, but with the HTTP headers
    *    applied to it. 
    */
   private HttpRequestBase applyHTTPHeadersToRequest(HttpRequestBase httpRequest, BJADWebRequest opRequest)
   {
      if (opRequest.getHeaders() != null)
      {
         for (Entry<String, String> header : opRequest.getHeaders().entrySet())
         {
            httpRequest.addHeader(header.getKey(), header.getValue());
         }
      }
      return httpRequest;
   }
   
   /**
    * Places the body from the request object into the HTTP 
    * request being built. 
    * 
    * @param opRequest
    *    The request for the operation containing the body to send and 
    *    potentially the BodyToString converter for unknown types.
    * @param body
    *    The body bean to apply to the rwequest being built.
    * @return
    *    The http request base with the body added to it if the 
    *    conditions were correct.
    */
   private HttpRequestBase applyBodyToRequest(HttpRequestBase request, BJADWebRequest opRequest, Object body) throws Exception
   {
      String bodyMessage = "";
      
      if (request instanceof HttpEntityEnclosingRequest)
      {
         if (body == null)
         {
            throw new IllegalArgumentException(
                  "Cannot complete a " + opRequest.getMethod().name() + " operation without some form of body.");
         }
         HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest)request;
         if (body instanceof HttpEntity)
         {
            entityRequest.setEntity((HttpEntity)body);
            bodyMessage = "Externally constructed " + body.getClass().getCanonicalName() + " instance";
         }
         else if (body instanceof ObjectJSONStringBody)
         {
            ObjectJSONStringBody model = (ObjectJSONStringBody)body;
            // Set the JSON converter to match the converter in the request if the 
            // model is set to do so. 
            if (model.useConverterFromRequest())
            {
               model.setObjectToJsonConverter(opRequest.getJsonObjectMapper());
            }
            entityRequest.setEntity(model.getEntity());
            bodyMessage = model.getLogString();
         }   
         else if (body instanceof AbstractBodyModel)
         {
            AbstractBodyModel model = (AbstractBodyModel)body;
            entityRequest.setEntity(model.getEntity());
            bodyMessage = model.getLogString();
         }         
      }
      else
      {
         if (body != null)
         {
            throw new IllegalArgumentException(
                  "Cannot complete a " + opRequest.getMethod().name() + " operation with a body.");
         }
      }
      
      logSendorRecv("SEND", opRequest.getUrl(), opRequest.getMethod(), opRequest.getHeaders(), bodyMessage);
      
      return request;
   }
   
   private void logSendorRecv(String direction, String url, HTTPMethodType method,  Map<String, String> headers, String bodyMessage) throws BJADWebException
   {
      try
      {
         logger.info(direction + " :: " + url + " (" + method.name() + ")" +
            " :: " + (headers == null ? "<n/a>" : operationRequest.getJsonObjectMapper().writeValueAsString(headers)) + 
            " :: " + (TextUtils.isBlank(bodyMessage) ? "" : bodyMessage));
      }
      catch (JsonProcessingException ex)
      {
         throw new BJADWebException(ex);
      }
   }
}
