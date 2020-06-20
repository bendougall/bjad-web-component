package bjad.web;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.net.SocketTimeoutException;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the BJADWebException class. 
 *
 * @author 
 *  Ben Dougall
 */
public class BJADWebExceptionTests
{
   /**
    * Tests message constructor.
    */
   @Test
   public void testMessageConstructor()
   {
      BJADWebException ex = new BJADWebException("This is a test");
      assertThat("Message was set correctly", ex.getMessage(), is("This is a test"));
      assertThat("No Cause was found", ex.getCause(), nullValue());
   }
   
   /**
    * Tests cause only constructor.
    */
   @Test
   public void testCauseConstructor()
   {
      IllegalArgumentException cause = new IllegalArgumentException();
      BJADWebException ex = new BJADWebException(cause);
      assertThat("Message was set correctly", ex.getMessage(), is("BJADWebException thrown due to " + cause.getClass().getCanonicalName()));
      assertThat("Cause was found", ex.getCause(), is(cause));
      
      BJADWebException ex1 = new BJADWebException(ex);
      assertThat("Message was set correctly", ex1.getMessage(), is("BJADWebException thrown due to " + ex.getClass().getCanonicalName()));
      assertThat("Cause was set to the original BJADWebException's cause", ex1.getCause(), is(cause));
   }
   
   /**
    * Tests the message and cause constructor.
    */
   @Test
   public void testMessageAndCauseConstructor()
   {
      BJADWebException ex = new BJADWebException("This is a test", null);
      assertThat("Message was set correctly", ex.getMessage(), is("This is a test"));
      assertThat("Cause was not found", ex.getCause(), nullValue());
      
      IllegalArgumentException cause = new IllegalArgumentException();
      ex = new BJADWebException("This is a test", cause);
      assertThat("Message was set correctly", ex.getMessage(), is("This is a test"));
      assertThat("Cause was found", ex.getCause(), is(cause));
   }
   
   /**
    * Verifies the isTimeoutException method
    */
   @Test
   public void testIsTimeoutExceptionMethod()
   {
      BJADWebException ex = new BJADWebException("TIMEOUT");
      assertThat("Exception with \"TIMEOUT\" string returns true", ex.isTimeoutException(), is(true));
      
      ex = new BJADWebException("Timed Out");
      assertThat("Exception with \"Timed Out\" string returns true", ex.isTimeoutException(), is(true));
      
      ex = new BJADWebException("Random String", new SocketTimeoutException("Test"));
      assertThat("Exception with cause set to SocketTimeoutException returns true", ex.isTimeoutException(), is(true));
      
      ex = new BJADWebException(new IllegalArgumentException(new SocketTimeoutException()));
      assertThat("Exception with cause's cause set to SocketTimeoutException returns true", ex.isTimeoutException(), is(true));
      
      ex = new BJADWebException("Not a exception that triggers the positive return", new IllegalArgumentException());
      assertThat("Exception without timeout triggers returns false", ex.isTimeoutException(), is(false));
   }
}
