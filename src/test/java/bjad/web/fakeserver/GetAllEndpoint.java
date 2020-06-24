package bjad.web.fakeserver;

import bjad.web.HTTPMethodType;
import bjad.web.model.Person;
import bjad.web.model.PersonList;
import spark.Request;
import spark.Response;

/**
 *  
 *
 * @author 
 *  Ben Dougall
 *
 */
public class GetAllEndpoint extends FakeHTTPServerEndpoint
{

   /**
    * @see spark.Route#handle(spark.Request, spark.Response)
    */
   @Override
   public Object handle(Request request, Response response) throws Exception
   {
      PersonList peopleList = new PersonList();
      for (Person p : PEOPLE.values())
      {
         peopleList.getPersons().add(p);
      }
      
      return GSON.writeValueAsString(peopleList);
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
      return HTTPMethodType.GET;
   }
}
