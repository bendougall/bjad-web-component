package bjad.web;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bjad.web.fakeserver.FakeHTTPServer;
import bjad.web.model.Person;
import bjad.web.model.PersonList;
import bjad.web.properties.EnhancedPropertyHelper;

/**
 * Unit tests for the BJADWebComponent class using requests 
 * coming from property files.  
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebComponentWithPropsTests
{   
   private static BJADWebResponse<PersonList> GET_ALL_RESULT;
   private static BJADWebResponse<Person> GET_RESULT;
   
   /**
    * Starts the fake HTTP server prior to all the test
    * cases executing.
    * 
    * @throws Exception
    *    Any exceptions during the start up of the tests will be thrown.
    */
   @BeforeAll
   public static void testsStarting() throws Exception
   {
      try
      {
         FakeHTTPServer.startServer(52526);
         
         EnhancedPropertyHelper props = new EnhancedPropertyHelper();
         props.loadPropertiesFromClasspathFile("WebProps/Web.GetAll.properties");
         
         BJADWebRequest req = BJADWebRequestFactory.createRequest(props);
         BJADWebComponent component = new BJADWebComponent(req);
         GET_ALL_RESULT = component.performWebCall(PersonList.class);
         
         Properties p = new Properties();
         p.put("person.id", "00000000-0000-0000-0000-000000000000");
         props = new EnhancedPropertyHelper();
         props.loadProperties(p);
         props.loadPropertiesFromClasspathFile("WebProps/Web.Get.properties");
         
         req = BJADWebRequestFactory.createRequest(props);
         component = new BJADWebComponent(req);
         GET_RESULT = component.performWebCall(Person.class);
         
         FakeHTTPServer.stopServer();
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         throw ex;
      }
   }
   
   /**
    * Verifies the get all result from the startup 
    */
   @Test
   public void verifyGetAllResults()
   {
      assertThat("Get All operation returned", GET_ALL_RESULT.isGoodResponse(), is(true));
      assertThat("List of people should not be null", GET_ALL_RESULT.getData(), notNullValue());
      assertThat("List of people should be at least 2 people", GET_ALL_RESULT.getData().getPersons().size(), greaterThanOrEqualTo(2));
   }
   
   /**
    * Verifies the get result from the startup 
    */
   @Test
   public void verifyGetResults()
   {
      assertThat("Get operation returned", GET_ALL_RESULT.isGoodResponse(), is(true));
      assertThat("Person should not be null", GET_ALL_RESULT.getData(), notNullValue());
      assertThat("Person's ID should be 00000000-0000-0000-0000-000000000000", GET_RESULT.getData().getId(), is("00000000-0000-0000-0000-000000000000"));
   }
}