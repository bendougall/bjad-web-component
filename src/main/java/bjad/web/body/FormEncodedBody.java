package bjad.web.body;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

import bjad.web.BJADWebException;

/**
 * Bean used for to represent form encoded data 
 * being sent as the body for the web request.
 *
 * @author 
 *  Ben Dougall
 */
public class FormEncodedBody extends AbstractBodyModel
{
   /**
    * The list of name value pairs to add to the entity.
    */
   protected List<NameValuePair> values;
   
   /**
    * The character set to apply within the entity being built.
    */
   protected String characterSet = null;
   
   /**
    * Creates an empty list of values 
    */
   public FormEncodedBody()
   {
      this.values = new ArrayList<>();
   }
   
   /**
    * Creates an empty list of values 
    * @param characterSet
    *    The character set to use within the entity.
    */
   public FormEncodedBody(String characterSet)
   {
      this.setCharacterSet(characterSet);
      this.values = new ArrayList<>();
   }
   
   /**
    * Creates the list of values from iterable name/value pairings
    * passed.
    *
    * @param values
    *    The name/value pairings to add to the entity
    */
   public FormEncodedBody(Iterable<? extends NameValuePair> values)
   {
      this();
      for (NameValuePair v : values)
      {
         this.values.add(v);
      }
   }
   
   /**
    * Creates the list of values from iterable name/value pairings
    * passed.
    *
    * @param values
    *    The name/value pairings to add to the entity
    * @param characterSet
    *    The character set to use within the entity.
    */
   public FormEncodedBody(Iterable<? extends NameValuePair> values, String characterSet)
   {
      this();
      this.setCharacterSet(characterSet);
      for (NameValuePair v : values)
      {
         this.values.add(v);
      }
   }
   
   /**
    * Creates the list of values from the mapping of Strings and 
    * Strings representing the keys and values.
    *
    * @param values
    *    The mapping of keys and values.
    */
   public FormEncodedBody(Map<String, String> values)
   {
      this();
      for (Entry<String, String> entry : values.entrySet())
      {
         this.values.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }
   }
   
   /**
    * Creates the list of values from the mapping of Strings and 
    * Strings representing the keys and values.
    *
    * @param values
    *    The mapping of keys and values.
    * @param characterSet
    *    The character set to use within the entity.
    */
   public FormEncodedBody(Map<String, String> values, String characterSet)
   {
      this();
      this.setCharacterSet(characterSet);
      for (Entry<String, String> entry : values.entrySet())
      {
         this.values.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
      }
   }
   
   /**
    * Adds a key and value pairing to the list.
    * 
    * @param name
    *    The name/key for the name/value pair.
    * @param value
    *    The value for the name/value pair.
    */
   public void addValue(String name, String value)
   {
      this.values.add(new BasicNameValuePair(name, value));
   }
   
   /**
    * Returns the string representation of the data in the bean.    * 
    * @return
    *    The string showing the number of items, and each of
    *    the items in the bean.
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<").append(values.size()).append("> ");
      sb.append("Form Encoded values: ");
      for (NameValuePair v : values)
      {
         sb.append("[\"").append(v.getName()).append('\"').append(']');
         sb.append(',').append(' ');
         sb.append("[\"").append(v.getValue()).append('\"').append(']');
      }
      
      return sb.toString();
   }

   /**
    * Returns the entity to be applied to the HTTP request.
    * 
    * @return
    *    The UrlEncodedFormEntity to apply to the http request.
    * @throws BJADWebException
    *    If the entity cannot be constructed with the passed 
    *    character set, this exception will be thrown.
    */
   @Override
   public HttpEntity getEntity() throws BJADWebException
   {
      try
      {
         return new UrlEncodedFormEntity(values, this.getCharacterSet());
      }
      catch (Exception ex)
      {
         throw new BJADWebException("Could not create the UrlEncodedFormEntity.", ex);
      }
   }

   /**
    * @see bjad.web.body.AbstractBodyModel#getLogString()
    */
   @Override
   public String getLogString()
   {
      return this.toString();
   }

   /**
    * @return 
    *   The characterSet property within the FormEncodedBody instance. If
    *   the character set is null or whitespace, UTF-8 will be set 
    *   by default.
    */
   public String getCharacterSet()
   {
      if (TextUtils.isBlank(this.characterSet))
      {
         this.setCharacterSet("UTF-8");
      }
      return this.characterSet;
   }

   /**
    * @param characterSet 
    *   The characterSet to set within the FormEncodedBody instance
    */
   public void setCharacterSet(String characterSet)
   {
      this.characterSet = characterSet;
   }
}
