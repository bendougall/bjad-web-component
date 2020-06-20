package bjad.web;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;

import bjad.web.body.ObjectJSONStringBody;
import bjad.web.body.StringBody;
import bjad.web.fakeserver.FakeHTTPServer;
import bjad.web.model.Person;
import bjad.web.model.PersonList;

/**
 * Unit tests for the BJADWebComponent class. 
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebComponentTests
{   
   /**
    * Starts the fake HTTP server prior to all the test
    * cases executing.
    */
   @BeforeAll
   public static void testsStarting()
   {
      FakeHTTPServer.startServer(52525);
   }
   
   /**
    * Stops the Fake HTTP server after all the test cases execute.
    */
   @AfterAll
   public static void testsFinished()
   {
      FakeHTTPServer.stopServer();
   }
   
   /**
    * Tests the https operations against google.ca
    *  
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail.
    */
   @Test
   public void testHttpsOperation() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("https://google.ca");
      req.setMethod(HTTPMethodType.GET);
      req.setCharacterSetForResponse("");
      
      BJADWebComponent component = new BJADWebComponent();
      component.setRequest(req);
      
      BJADWebResponse<String> response = component.performWebCall(String.class);
      assertThat("Good Return Code", response.isGoodResponse(), is(true));
      assertThat("Duration is more than 0", response.getDuration(), greaterThanOrEqualTo(1L));
      String text = response.getData();
      assertThat("Google home page downloaded with content", text != null && !text.trim().isEmpty(), is(true));
   }
   
   /**
    * tests the https operation using an overridden ssl context.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail.
    */
   @Test
   public void testHttpsWithOverridenSSL() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("https://google.ca");
      req.setMethod(HTTPMethodType.GET);
      req.setSslContext(SSLContext.getDefault());
      
      BJADWebComponent component = new BJADWebComponent();
      component.setRequest(req);
      
      BJADWebResponse<String> response = component.performWebCall(String.class);
      assertThat("Good Return Code", response.isGoodResponse(), is(true));
      assertThat("Duration is more than 0", response.getDuration(), greaterThanOrEqualTo(1L));
      String text = response.getData();
      assertThat("Google home page downloaded with content", text != null && !text.trim().isEmpty(), is(true));
   }
   /**
    * Tests the getAll operation in the fake http server
    *  
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail.
    */
   @Test
   public void testGetAllOperation() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.GET);
      Map<String, String> headers = new HashMap<>();
      headers.put("MessageID", "HeaderTest");
      req.setHeaders(headers);
      req.setGsonImplementation(new GsonBuilder().disableHtmlEscaping().create());
      req.setCharacterSetForResponse("UTF-8");
      
      BJADWebComponent component = new BJADWebComponent();
      component.setRequest(req);
      
      String text = component.performWebCall(String.class).getData();
      assertThat("Get All operation returned", text != null && !text.trim().isEmpty(), is(true));
      PersonList people = req.getGsonImplementation().fromJson(text, PersonList.class);
      assertThat("List of people should not be null", people, notNullValue());
      assertThat("List of people should be at least 2 people", people.getPersons().size(), greaterThanOrEqualTo(2));
   }
   
   /**
    * Tests the get operation for a specific person
    * via the fake http server
    *  
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail.
    */
   @Test
   public void testGetPersonOperation() throws Exception
   {
      String id = "00000000-0000-0000-0000-000000000000";
      BJADWebRequest req = new BJADWebRequest();
      req.setLoggerName(null);
      req.setConnectionTimeout(-300);
      req.setReadTimeout(-300);
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.GET);
      
      BJADWebComponent component = new BJADWebComponent(req);
      
      byte[] bytes = component.performWebCall(byte[].class).getData();
      assertThat("Get by ID operation returned", bytes != null && bytes.length > 0, is(true));
      
      try (
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            InputStreamReader isr = new InputStreamReader(bis)
          )
      {
         Person p = req.getGsonImplementation().fromJson(isr, Person.class);
         assertThat("person's id should match the one requested", p.getId(), is(id));
         assertThat("person's first name should match known value \"First\"", p.getFirstName(), is("First"));
         assertThat("person's last name should match know value \"User\"", p.getLastName(), is("User"));
      }
   }
   
   /**
    * Tests the POST operation to add a new employee.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testAddNewPerson() throws Exception
   {
      Person p = new Person(null, "New", "User");
      
      BJADWebRequest req = new BJADWebRequest();
      req.setLoggerName(BJADWebComponent.class.getCanonicalName());
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.POST);
      
      BJADWebComponent component = new BJADWebComponent(req);
      
      String text = component.performWebCall(String.class, new ObjectJSONStringBody(p, "ISO_8859_1")).getData();
      assertThat("Add new person operation returned", text != null && !text.trim().isEmpty(), is(true));
      p = req.getGsonImplementation().fromJson(text, Person.class);
      assertThat("Add new person operation returned a random id", p.getId() != null && !p.getId().trim().isEmpty(), is(true));
      
      p = new Person("Another", "Newbie");
      String id = p.getId();
      
      req = new BJADWebRequest();
      req.setLoggerName("");
      req.setConnectionTimeout(1000);
      req.setReadTimeout(5000);
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.POST);
      
      component = new BJADWebComponent(req);
      
      text = component.performWebCall(String.class, new StringEntity(req.getGsonImplementation().toJson(p), StandardCharsets.ISO_8859_1)).getData();
      assertThat("Add new person operation returned", text != null && !text.trim().isEmpty(), is(true));
      p = req.getGsonImplementation().fromJson(text, Person.class);
      assertThat("Add new person operation returned a random id", p.getId(), is(id));
      
      p = new Person("Super", "Newb");
      id = p.getId();
      
      req = new BJADWebRequest();
      req.setLoggerName("");
      req.setConnectionTimeout(1000);
      req.setReadTimeout(5000);
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.POST);
      
      component = new BJADWebComponent(req);
      
      text = component.performWebCall(String.class, new StringBody(req.getGsonImplementation().toJson(p))).getData();
      assertThat("Add new person operation returned", text != null && !text.trim().isEmpty(), is(true));
      p = req.getGsonImplementation().fromJson(text, Person.class);
      assertThat("Add new person operation returned a random id", p.getId(), is(id));
      
      p = new Person("Super", "Newb");
      id = p.getId();
      
      req = new BJADWebRequest();
      req.setLoggerName("");
      req.setConnectionTimeout(1000);
      req.setReadTimeout(5000);
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.POST);
      
      component = new BJADWebComponent(req);
      
      text = component.performWebCall(String.class, new StringBody(req.getGsonImplementation().toJson(p), "ISO_8859_1")).getData();
      assertThat("Add new person operation returned", text != null && !text.trim().isEmpty(), is(true));
      p = req.getGsonImplementation().fromJson(text, Person.class);
      assertThat("Add new person operation returned a random id", p.getId(), is(id));
      
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.GET);
      
      component = new BJADWebComponent(req);
      
      text = component.performWebCall(String.class).getData();
      assertThat("Get All operation returned", text != null && !text.trim().isEmpty(), is(true));
      PersonList people = req.getGsonImplementation().fromJson(text, PersonList.class);
      assertThat("List of people should not be null", people, notNullValue());
      assertThat("List of people should be at least 6 people", people.getPersons().size(), greaterThanOrEqualTo(6));
   }
   
   /**
    * Tests the PUT operations within the component.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testPutOperation() throws Exception
   {
      String id = "00000000-0000-0000-0000-000000000000";
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.GET);
      
      BJADWebComponent component = new BJADWebComponent(req);
      Person p = component.performWebCall(Person.class).getData();
      p.setFirstName("FirstName");
      p.setLastName("Modified");
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.PUT);
      
      component.setRequest(req);
      component.performWebCall(byte[].class, new ObjectJSONStringBody(p)).getData();
      
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.GET);
      
      component = new BJADWebComponent(req);
      p = component.performWebCall(Person.class).getData();
      
      assertThat("First Name has modified by PUT command.", p.getFirstName(), is("FirstName"));
      assertThat("Last Name has modified by PUT command.", p.getLastName(), is("Modified"));
   }
   
   /**
    * Tests the PATCH operations within the component.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testPatchOperation() throws Exception
   {
      String id = "11111111-1111-1111-1111-111111111111";
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.GET);
      
      BJADWebComponent component = new BJADWebComponent(req);
      Person p = component.performWebCall(Person.class).getData();
      p.setFirstName("Modified");
      p.setLastName("LastName");
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.PATCH);
      
      component.setRequest(req);
      component.performWebCall(byte[].class, new ObjectJSONStringBody(p)).getData();
      
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + id);
      req.setMethod(HTTPMethodType.GET);
      
      component = new BJADWebComponent(req);
      p = component.performWebCall(Person.class).getData();
      
      assertThat("First Name has modified by PUT command.", p.getFirstName(), is("Modified"));
      assertThat("Last Name has modified by PUT command.", p.getLastName(), is("LastName"));
   }
   
   /**
    * Tests the TRACE option 
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testTraceOperation() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/");
      req.setMethod(HTTPMethodType.TRACE);
      
      BJADWebComponent component = new BJADWebComponent(req);
      component.performWebCall(String.class).getData();
   }
   
   /**
    * Tests the TRACE option 
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testOptionsOperation() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/");
      req.setMethod(HTTPMethodType.OPTIONS);
      req.setCharacterSetForResponse("UTF-8");
      
      BJADWebComponent component = new BJADWebComponent(req);
      BJADWebResponse<String> response = component.performWebCall( String.class);
      assertThat("Good Return Code", response.isGoodResponse(), is(true));
      assertThat("Headers contain Allow", response.getHeaders().containsKey("Allow"), is(true));
   }
   
   /**
    * Tests the HEAD option 
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testHeadOperation() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.HEAD);
      
      BJADWebComponent component = new BJADWebComponent(req);
      component.performWebCall(String.class).getData();
   }
   
   /**
    * Tests the delete operation.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail 
    */
   @Test
   public void deletePerson() throws Exception
   {
      Person p = new Person(null, "New", "User");
      
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.POST);
      
      BJADWebComponent component = new BJADWebComponent(req);
      
      String text = component.performWebCall(String.class, new ObjectJSONStringBody(p)).getData();
      assertThat("Add new person operation returned", text != null && !text.trim().isEmpty(), is(true));
      p = req.getGsonImplementation().fromJson(text, Person.class);
      assertThat("Add new person operation returned a random id", p.getId() != null && !p.getId().trim().isEmpty(), is(true));
            
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + p.getId());
      req.setMethod(HTTPMethodType.DELETE);
      component.setRequest(req);
      component.performWebCall(byte[].class).getData();
      
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person/" + p.getId());
      req.setMethod(HTTPMethodType.GET);
      component.setRequest(req);
      BJADWebResponse<String> response = component.performWebCall(String.class);
      
      assertThat("IsGoodResponse should be false after retrieving deleted user", response.isGoodResponse(), is(false));
      response.setStatusCode(100);
      assertThat("IsGoodResponse should be false after setting status code to 100", response.isGoodResponse(), is(false));
   }
   
   /**
    * Tests the various exception cases within 
    * the web component.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and cause the test
    *    to fail
    */
   @Test
   public void testExceptionCases() throws Exception
   {
      BJADWebRequest req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.GET);
      req.setCharacterSetForResponse("WAKAWAKA");
      
      BJADWebComponent component = new BJADWebComponent();
      component.setRequest(req);
      
      Assertions.assertThrows(BJADWebException.class, () -> {
         component.performWebCall(String.class);
       }, "Should throw BJADWebException when an unknown encoding type is passed.");
            
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.PATCH);
      component.setRequest(req);
      
      Assertions.assertThrows(BJADWebException.class, () -> {
         component.performWebCall(byte[].class).getData();
       }, "Should throw BJADWebException when Body Required Method used out without a body");       
      
      req = new BJADWebRequest();
      req.setUrl("http://localhost:52525/person");
      req.setMethod(HTTPMethodType.GET);
      component.setRequest(req);
      
      Assertions.assertThrows(BJADWebException.class, () -> {
         component.performWebCall(byte[].class, new ObjectJSONStringBody(new Person("Should", "Fail"))).getData();
       }, "Should throw BJADWebException when no body method is used with a body passed.");
      
      assertThat("Response's header mapping is never null", new BJADWebResponse<>().getHeaders(), notNullValue());
   }
}
