package bjad.web.fakeserver;

import bjad.web.HTTPMethodType;
import spark.Request;
import spark.Response;

/**
 *  
 *
 * @author 
 *  Ben Dougall
 *
 */
public class HeadEndpoint extends FakeHTTPServerEndpoint
{

   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      response.status(200);
      response.header("PersonCount", String.valueOf(PEOPLE.size()));
      return "";
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
      return HTTPMethodType.HEAD;
   }
}
