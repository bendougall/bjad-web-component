package bjad.web.fakeserver;

import bjad.web.HTTPMethodType;
import bjad.web.model.Person;
import spark.Request;
import spark.Response;

/**
 * PUT endpoint for updating a person within the 
 * person collection.
 *
 * @author 
 *  Ben Dougall
 */
public class PutPersonEndpoint extends FakeHTTPServerEndpoint
{
   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      Person returnVal = PEOPLE.get(request.params("id"));
      Person p = GSON.readValue(request.body(), Person.class);
      if (returnVal != null)
      {
         PEOPLE.put(returnVal.getId(), p);
         response.status(200);
         return "";
      }
      else
      {
         response.status(201);
         PEOPLE.put(p.getId(), p);
         return "";
      }    
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getEndpointName()
    */
   @Override
   public String getEndpointName()
   {
      return "/person/:id";
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getHttpMethod()
    */
   @Override
   public HTTPMethodType getHttpMethod()
   {
      return HTTPMethodType.PUT;
   }

}
