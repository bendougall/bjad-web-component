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
public class TraceEndpoint extends FakeHTTPServerEndpoint
{

   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      response.status(200);
      return "";
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getEndpointName()
    */
   @Override
   public String getEndpointName()
   {
      return "/";
   }

   /**
    * @see bjad.web.fakeserver.FakeHTTPServerEndpoint#getHttpMethod()
    */
   @Override
   public HTTPMethodType getHttpMethod()
   {
      return HTTPMethodType.TRACE;
   }
}
