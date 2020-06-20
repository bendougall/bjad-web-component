package bjad.web.fakeserver;

import bjad.web.HTTPMethodType;
import bjad.web.model.Person;
import spark.Request;
import spark.Response;

/**
 *  
 *
 * @author 
 *  Ben Dougall
 *
 */
public class GetByIDEndpoint extends FakeHTTPServerEndpoint
{

   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      Person returnVal = PEOPLE.get(request.params("id"));
      if (returnVal != null)
      {
         response.status(200);
         return GSON.toJson(returnVal);
      }
      else
      {
         response.status(404);
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
      return HTTPMethodType.GET;
   }
}
