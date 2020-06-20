package bjad.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response object from the BJADWebComponent when 
 * data is retrieved from a web server.
 *  
 *
 * @author 
 *  Ben Dougall
 *
 * @param <T>
 *    The type of object that response will
 *    contain.
 */
public class BJADWebResponse<T>
{
   /**
    * The status code returned by the HTTP 
    * server as a result of the operation.
    */
   protected int statusCode;
   /**
    * The number of milliseconds the operation 
    * took.
    */
   protected long duration;
   /**
    * The headers returned by the HTTP server
    * as a result of the operation.
    */
   protected Map<String, String> headers;
   /**
    * The data/body returned by the body.
    */
   protected T data;
   
   /**
    * @return 
    *   The statusCode property within the WebOperationResponse instance
    */
   public int getStatusCode()
   {
      return this.statusCode;
   }
   /**
    * @param statusCode 
    *   The statusCode to set within the WebOperationResponse instance
    */
   public void setStatusCode(int statusCode)
   {
      this.statusCode = statusCode;
   }
   /**
    * @return 
    *   The duration property within the WebOperationResponse instance
    */
   public long getDuration()
   {
      return this.duration;
   }
   /**
    * @param duration 
    *   The duration to set within the WebOperationResponse instance
    */
   public void setDuration(long duration)
   {
      this.duration = duration;
   }
   /**
    * @return 
    *   The headers property within the WebOperationResponse instance
    */
   public Map<String, String> getHeaders()
   {
      if (headers == null)
      {
         headers = new LinkedHashMap<String, String>();
      }
      return this.headers;
   }
   /**
    * @param headers 
    *   The headers to set within the WebOperationResponse instance
    */
   public void setHeaders(Map<String, String> headers)
   {
      this.headers = headers;
   }
   /**
    * @return 
    *   The data property within the WebOperationResponse instance
    */
   public T getData()
   {
      return this.data;
   }
   /**
    * @param data 
    *   The data to set within the WebOperationResponse instance
    */
   public void setData(T data)
   {
      this.data = data;
   }
   
   /**
    * Copies the non data values from the source response object
    * to the current one.
    * 
    * @param source
    *    The response object to copy the values from.
    */
   public void copyNonDataValues(BJADWebResponse<?> source)
   {
      this.duration = source.getDuration();
      this.statusCode = source.getStatusCode();
      this.headers = source.getHeaders();
   }
   
   /**
    * Returns if the response object represents a good HTTP
    * response by checking if the status is within the 200s
    * range.
    * 
    * @return
    *    True if the status code is between 200 and 299.
    */
   public boolean isGoodResponse()
   {
      return getStatusCode() > 199 && getStatusCode() < 300;
   }
}
