package bjad.web.body;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bjad.web.BJADWebException;

/**
 * Body Model representing a object that will be converted 
 * into a JSON string.
 *
 * @author 
 *  Ben Dougall
 */
public class ObjectJSONStringBody extends AbstractBodyModel
{
   /**
    * The object that will be made into a
    * JSON string that is the body for the HTTP operation.
    */
   protected Object body;
   /**
    * The character set to use for the string body
    */
   protected String characterSet;
   
   /**
    * The converter to use to make the object 
    * into a JSON string.
    */
   protected ObjectMapper objectToJsonConverter;
   
   /**
    * Flag used to determine if the JSON converter
    * from the request will be used for the JSON 
    * conversion. If set to true, or if set to false
    * but the objectToJsonConverter is null, the 
    * Component will set the converter to the one
    * provided in the WebRequest.
    */
   protected boolean useConverterFromRequest = true;
   
   /**
    * Default constructor, setting the properties
    * to a blank string and the default character set.
    */
   public ObjectJSONStringBody()
   {
      this(null, null, null);
   }
   
   /**
    * Constructor, setting the body property for 
    * the object, and set to use the default 
    * character set. 
    * 
    * @param body
    *    The object body to apply to the model.
    */
   public ObjectJSONStringBody(Object body)
   {
      this(body, null, null);
   }

   /**
    * Custom constructor, setting both the body and 
    * character properties. 
    * 
    * @param body
    *    The object body to apply to the model
    * @param characterSet
    *    The character set to apply to the model.
    */
   public ObjectJSONStringBody(Object body, String characterSet)
   {
      this(body, characterSet, null);
   }
   
   /**
    * Custom constructor, setting both the body and 
    * object to json converter properties. 
    * 
    * @param body
    *    The object body to apply to the model
    * @param converter
    *    The Object to JSON String converter to use. If
    *    not null, the use converter from request flag 
    *    will be set to false.
    */
   public ObjectJSONStringBody(Object body, ObjectMapper converter)
   {
      this(body, null, converter);
   }
   
   /**
    * Custom constructor, setting the body, character set,
    * and JSON converter properties. 
    * 
    * @param body
    *    The object body to apply to the model
    * @param characterSet
    *    The character set to apply to the model.
    * @param converter
    *    The Object to JSON String converter to use. If
    *    not null, the use converter from request flag 
    *    will be set to false.
    */
   public ObjectJSONStringBody(Object body, String characterSet, ObjectMapper converter)
   {
      this.setBody(body);
      this.setCharacterSet(characterSet);
      this.setObjectToJsonConverter(converter);
      this.setUseConverterFromRequest(converter == null);
   }
   
   /**
    * @return 
    *   The body property within the ObjectJSONStringBody instance
    */
   public Object getBody()
   {
      if (this.body == null)
      {
         this.body = new Object();
      }
      return this.body;
   }

   /**
    * @param body 
    *   The body to set within the ObjectJSONStringBody instance
    */
   public void setBody(Object body)
   {    
      this.body = body;
   }

   /**
    * @return 
    *   The characterSet property within the ObjectJSONStringBody instance
    */
   public String getCharacterSet()
   {
      if (TextUtils.isBlank(characterSet))
      {
         this.setCharacterSet("UTF-8");
      }
      return this.characterSet;
   }

   /**
    * @param characterSet 
    *   The characterSet to set within the ObjectJSONStringBody instance
    */
   public void setCharacterSet(String characterSet)
   {
      this.characterSet = characterSet;
   }

   /**
    * @return 
    *   The objectToJsonConverter property within the ObjectJSONStringBody instance
    */
   public ObjectMapper getObjectToJsonConverter()
   {
      return this.objectToJsonConverter;
   }

   /**
    * @param objectToJsonConverter 
    *   The objectToJsonConverter to set within the ObjectJSONStringBody instance
    */
   public void setObjectToJsonConverter(ObjectMapper objectToJsonConverter)
   {
      this.objectToJsonConverter = objectToJsonConverter;
   }

   /**
    * @return 
    *   The useConverterFromRequest property within the ObjectJSONStringBody instance
    */
   public boolean useConverterFromRequest()
   {
      return this.useConverterFromRequest || objectToJsonConverter == null;
   }

   /**
    * @param useConverterFromRequest 
    *   The useConverterFromRequest to set within the ObjectJSONStringBody instance
    */
   public void setUseConverterFromRequest(boolean useConverterFromRequest)
   {
      this.useConverterFromRequest = useConverterFromRequest;
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getEntity()
    */
   @Override
   public HttpEntity getEntity() throws BJADWebException
   {
      try
      {
         return new StringEntity(getObjectToJsonConverter().writeValueAsString(this.getBody()), this.getCharacterSet());
      }
      catch (JsonProcessingException ex)
      {
         throw new BJADWebException(ex);
      }
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getLogString()
    */
   @Override
   public String getLogString()
   {
      try
      {
         return this.getCharacterSet() + " JSON String of " +
            this.getBody().getClass().getCanonicalName() + 
            ": " + getObjectToJsonConverter().writeValueAsString(this.getBody());
      }
      catch (JsonProcessingException ex)
      {
         return this.getCharacterSet() + " JSON String of " +
               this.getBody().getClass().getCanonicalName() + 
               ": !!! failed to make JSON string due to " + ex.getClass().getSimpleName() + ", msg: " + ex.getMessage();
      }
   }
}
