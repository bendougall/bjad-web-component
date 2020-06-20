package bjad.web;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *  
 *
 * @author 
 *  Ben Dougall
 *
 */
public class MediaTypeTests
{
   /** 
    * Tests the constructor without charset information 
    * passed in the constructor.
    */
   @Test
   public void testWithoutCharset()
   {
      MediaType mt = new MediaType("APPLICATION/JSON");
      String result = mt.toString();
      assertThat("Media Type's toString() returns application/json", result, is("application/json"));
      mt = mt.addCharset("utf-8");
      result = mt.toString();
      assertThat("Media Type's toString() after adding utf-8 charset returns application/json; charset=UTF-8",
            result,
            is("application/json; charset=UTF-8"));
   }
   
   /** 
    * Tests the constructor with charset information 
    * passed in the constructor.
    */
   @Test
   public void testWithCharset()
   {
      MediaType mt = new MediaType("APPLICATION/JSON", "UTF-8");
      assertThat("Media Type's toString() returns application/json; charset=UTF-8",
            mt.toString(),
            is("application/json; charset=UTF-8"));
      
      mt = new MediaType("APPLICATION/JSON", "   ");
      assertThat("Media Type's toString() returns application/json; charset=UTF-8",
            mt.toString(),
            is("application/json"));
   }
   
   /**
    * Tests the imageType factory method.
    */
   @Test
   public void testImageFactory()
   {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
         MediaType.imageType(null);
       }, "Should throw IllegalArgumentException with null passed");
      
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
         MediaType.imageType("   ");
       }, "Should throw IllegalArgumentException with blank string passed");
      
      MediaType mt = MediaType.imageType("JPEG");
      assertThat("Media Type's toString() returns image/jpeg",
            mt.toString(),
            is("image/jpeg"));
   }
}
