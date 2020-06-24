package bjad.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import bjad.web.provider.ObjectToJSONProvider;

/**
 * Implementation of the ObjectToJSONProvider.
 *
 * @author 
 *  Ben Dougall
 */
public class TestObject2JsonProvider implements ObjectToJSONProvider
{
   /**
    * @see bjad.web.provider.ObjectToJSONProvider#getObjectToJSONMapper()
    */
   @Override
   public ObjectMapper getObjectToJSONMapper() throws BJADWebException
   {
      ObjectMapper o = new ObjectMapper();
      o.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return o;
   }
}
