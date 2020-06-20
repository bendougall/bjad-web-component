package bjad.web.fakeserver;

import java.util.LinkedHashSet;
import java.util.Set;

import bjad.web.HTTPMethodType;
import spark.Spark;

/**
 * The fkae http server class for 
 * starting and stopping the 
 * HTTP server.  
 *
 * @author 
 *  Ben Dougall
 */
public class FakeHTTPServer
{
   /**
    * The endpoint implementations to add to the FakeHTTPServer.
    */
   static final FakeHTTPServerEndpoint[] endpoints = new FakeHTTPServerEndpoint[] {
         new GetAllEndpoint(),
         new GetByIDEndpoint(),
         new AddPersonEndpoint(),
         new PutPersonEndpoint(),
         new PatchPersonEndpoint(),
         new TraceEndpoint(),
         new HeadEndpoint(),
         new DeleteEndpoint(),
   };
   
   /**
    * Starts the fake HTTP server.
    * @param port
    *    The port to run the server on.
    */
   public static void startServer(int port)
   {
      Spark.awaitStop();
      Spark.port(port);
      
      FakeHTTPServerEndpoint.log("Started HTTP Server against port " + port, FakeHTTPServer.class);
      
      Set<HTTPMethodType> methods = new LinkedHashSet<>();
      for (FakeHTTPServerEndpoint end : endpoints)
      {
         boolean acceptSet = end.getAcceptType() != null;
         switch (end.getHttpMethod())
         {
         case DELETE:
            if (acceptSet)
            {
               Spark.delete(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.delete(end.getEndpointName(), end);
            }
            break;
         case GET:
            if (acceptSet)
            {
               Spark.get(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.get(end.getEndpointName(), end);
            }
            break;
         case HEAD:
            if (acceptSet)
            {
               Spark.head(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.head(end.getEndpointName(), end);
            }
            break;
         case OPTIONS:
            if (acceptSet)
            {
               Spark.options(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.options(end.getEndpointName(), end);
            }
            break;
         case PATCH:
            if (acceptSet)
            {
               Spark.patch(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.patch(end.getEndpointName(), end);
            }
            break;
         case POST:
            if (acceptSet)
            {
               Spark.post(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.post(end.getEndpointName(), end);
            }
            break;
         case PUT:
            if (acceptSet)
            {
               Spark.put(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.put(end.getEndpointName(), end);
            }
            break;
         case TRACE:
            if (acceptSet)
            {
               Spark.trace(end.getEndpointName(), end.getAcceptType(), end);
            }
            else
            {
               Spark.trace(end.getEndpointName(), end);
            }
            break;
         default:
            String exMessage = String.format("HTTP Method Type for endpoint %s (class: %s) cannot be null.",
                  end.getEndpointName(),
                  end.getClass().getCanonicalName());
            throw new IllegalArgumentException(exMessage);
         }
         methods.add(end.getHttpMethod());
         
         String logMessage = String.format("Added %s endpoint \"%s%s\" against %s.",
               end.getHttpMethod().name(),
               end.getEndpointName(),
               end.getAcceptType() != null ? "/" + (end.getAcceptType()) : "",
               end.getClass());
         FakeHTTPServerEndpoint.log(logMessage, FakeHTTPServer.class);
      }
      
      // Add in the options endpoint with the options available from the 
      // endpoints added above.
      Spark.options("/", (req, res) -> 
      {
         StringBuilder sb = new StringBuilder();
         for (HTTPMethodType method : methods)
         {
            sb.append(", ").append(method.name());
         }
         sb.insert(0, ", OPTIONS");
         res.status(204);
         res.header("Allow", sb.toString().substring(2));
         return "";
      });
   }
   
   /** 
    * Stops the Fake HTTP server. 
    */
   public static void stopServer()
   {
      Spark.stop();
      FakeHTTPServerEndpoint.log("Stopped HTTP Server", FakeHTTPServer.class);
   }
}
