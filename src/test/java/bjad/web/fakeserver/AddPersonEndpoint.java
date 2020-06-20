package bjad.web.fakeserver;

import java.util.UUID;

import bjad.web.HTTPMethodType;
import bjad.web.model.Person;
import spark.Request;
import spark.Response;

/**
 * POST endpoint for adding a person to the 
 * person collection.
 *
 * @author 
 *  Ben Dougall
 */
public class AddPersonEndpoint extends FakeHTTPServerEndpoint
{
   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      Person p = GSON.fromJson(request.body(), Person.class);
      if (p.getId() == null || p.getId().trim().isEmpty())
      {
         p.setId(UUID.randomUUID().toString());
      }
      PEOPLE.put(p.getId(), p);
      response.status(201);
      return GSON.toJson(p);
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getEndpointName()
    */
   @Override
   public String getEndpointName()
   {
      return "/person";
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getHttpMethod()
    */
   @Override
   public HTTPMethodType getHttpMethod()
   {
      return HTTPMethodType.POST;
   }

}
