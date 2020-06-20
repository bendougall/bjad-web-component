package bjad.web.body;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.TextUtils;

import bjad.web.BJADWebException;

/**
 * Body Model representing a pre-built string 
 *
 * @author 
 *  Ben Dougall
 */
public class StringBody extends AbstractBodyModel
{
   /**
    * The string that is the body for the HTTP operation.
    */
   protected String body;
   /**
    * The character set to use for the string body
    */
   protected String characterSet;
   
   /**
    * Default constructor, setting the properties
    * to a blank string and the default character set.
    */
   public StringBody()
   {
      this(null, null);
   }
   
   /**
    * Constructor, setting the body property for 
    * the object, and set to use the default 
    * character set. 
    * 
    * @param body
    *    The string body to apply to the model.
    */
   public StringBody(String body)
   {
      this(body, null);
   }

   /**
    * Custom constructor, setting both the body and 
    * character properties. 
    * 
    * @param body
    *    The string body to apply to the model
    * @param characterSet
    *    The character set to apply to the model.
    */
   public StringBody(String body, String characterSet)
   {
      this.setBody(body);
      this.setCharacterSet(characterSet);
   }

   /**
    * @return 
    *   The body property within the StringBody instance
    */
   public String getBody()
   {
      if (this.body == null)
      {
         this.body = "";
      }
      return this.body;
   }

   /**
    * @param body 
    *   The body to set within the StringBody instance
    */
   public void setBody(String body)
   {    
      this.body = body;
   }

   /**
    * @return 
    *   The characterSet property within the StringBody instance
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
    *   The characterSet to set within the StringBody instance
    */
   public void setCharacterSet(String characterSet)
   {
      this.characterSet = characterSet;
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getEntity()
    */
   @Override
   public HttpEntity getEntity() throws BJADWebException
   {
      return new StringEntity(this.getBody(), this.getCharacterSet());
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getLogString()
    */
   @Override
   public String getLogString()
   {
      return this.getCharacterSet() + " String: " + this.getBody();
   }
}
