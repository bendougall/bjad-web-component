package bjad.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
   public Gson getObjectToJSONMapper() throws BJADWebException
   {
      return new GsonBuilder().disableHtmlEscaping().create();
   }
}
