package bjad.web.fakeserver;

import java.util.Date;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bjad.web.HTTPMethodType;
import bjad.web.model.Person;
import spark.Route;

/**
 * Abstract class for the http server to use 
 * to implement endpoints during the unit tests.
 *
 * @author 
 *  Ben Dougall
 */
public abstract class FakeHTTPServerEndpoint implements Route
{
   /**
    * Mapping of IDs and person beans.
    */
   protected static final LinkedHashMap<String, Person> PEOPLE = new LinkedHashMap<>();
   
   /**
    * GSON implementation to use.
    */
   protected static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
   
   /**
    * Creates a couple of person beans within the mapping. 
    */
   static 
   {
      Person p = new Person("00000000-0000-0000-0000-000000000000", "First", "User");
      PEOPLE.put(p.getId(), p);
      
      p = new Person("11111111-1111-1111-1111-111111111111", "Second", "User");
      PEOPLE.put(p.getId(), p);
   }
   
   /**
    * Gets the endpoint text to apply to the http
    * server in order for consumers to access
    * the endpoint.
    * 
    * @return
    *    the endpoint text (i.e. /test)
    */
   public abstract String getEndpointName();
   
   /**
    * Gets the type of HTTP method the 
    * endpoint will be accessible by.
    * 
    * @return
    *    The method type.
    */
   public abstract HTTPMethodType getHttpMethod();
   
   /**
    * Used to override the accept type for the endpoint,
    * for example, XML or JSON. 
    * 
    * @return
    *    The accept type for the endpoint, if null,
    *    any accept type will be used for the 
    *    endpoint.
    */
   public String getAcceptType()
   {
      return null;
   }
   
   /**
    * Logs a message to the console.
    * 
    * @param message
    *    The message to log. 
    */
   public void log(String message)
   {
      log(message, this.getClass());
   }
   
   /**
    * Logs a message to the console.
    * 
    * @param message
    *    The message to log. 
    * @param clazz
    *    The class the message is coming from.
    */
   public static void log(String message, Class<?> clazz)
   {
      String logLine = String.format("%s :: %s :: %s",
            new Date().toString(),
            clazz.getSimpleName(),
            message);
      System.out.println(logLine);
   }
}
