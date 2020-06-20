package bjad.web.body;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;

import bjad.web.BJADWebException;
import bjad.web.MediaType;

/**
 * Unit tests for the various classes in the 
 * body package.
 *
 * @author 
 *  Ben Dougall
 */
public class BodyTypeTests
{
   /**
    * Tests the string body class, including its constructors and 
    * its getters.
    */
   @Test
   public void testStringBody()
   {
      StringBody sb = new StringBody();
      assertThat("Default Ctor results in blank string body", sb.getBody(), is(""));
      assertThat("Default Ctor results in UTF-8 character set", sb.getCharacterSet(), is("UTF-8"));
      assertThat("Default Ctor results in \"UTF-8 String: \" as its getLogString result", sb.getLogString(), is("UTF-8 String: "));
      
      sb.setBody("Test");
      sb.setCharacterSet("ISO");
      
      assertThat("after setters, result is set string body", sb.getBody(), is("Test"));
      assertThat("after setters, result is set character set", sb.getCharacterSet(), is("ISO"));
      assertThat("after setters, result is \"ISO String: Test\" as its getLogString result", sb.getLogString(), is("ISO String: Test"));
   }
   
   /**
    * Tests the object to json string body class
    */
   @Test
   public void testObjectJSONBody()
   {
      ObjectJSONStringBody sb = new ObjectJSONStringBody();
      assertThat("Default Ctor results in UTF-8 character set", sb.getCharacterSet(), is("UTF-8"));
      assertThat("Use Converter from request flag is true for blank ctor", sb.useConverterFromRequest(), is(true));
      
      sb = new ObjectJSONStringBody(null, new GsonBuilder().disableHtmlEscaping().create());
      assertThat("Use Converter from request flag is false when impl provided", sb.useConverterFromRequest(), is(false));
      assertThat("Blank body does not return null.", sb.getBody(), notNullValue());
      
      sb.setUseConverterFromRequest(true);
      sb.setObjectToJsonConverter(null);
      assertThat("Use Converter from request should be true", sb.useConverterFromRequest(), is(true));
      
      sb.setUseConverterFromRequest(true);
      sb.setObjectToJsonConverter(new GsonBuilder().disableHtmlEscaping().create());
      assertThat("Use Converter from request should be true", sb.useConverterFromRequest(), is(true));
      
      sb.setUseConverterFromRequest(false);
      sb.setObjectToJsonConverter(null);
      assertThat("Use Converter from request should be true", sb.useConverterFromRequest(), is(true));
      
      sb.setUseConverterFromRequest(false);
      sb.setObjectToJsonConverter(new GsonBuilder().disableHtmlEscaping().create());
      assertThat("Use Converter from request should be false", sb.useConverterFromRequest(), is(false));
   }
   
   /**
    * Tests the form encoded body class.
    * @throws Exception
    *    Any exception will be thrown, failing the test case.
    */
   @Test
   public void testFormEncodedBody() throws Exception
   {
      FormEncodedBody feb = new FormEncodedBody();
      assertThat("Default Ctor results in UTF-8 character set", feb.getCharacterSet(), is("UTF-8"));
      assertThat("Default Ctor results in \"<0> Form Encoded values: \" as its getLogString result", feb.getLogString(), is("<0> Form Encoded values: "));
      
      feb = new FormEncodedBody("ISA");
      feb.addValue("Test", "Value");
      feb.setCharacterSet("ISO");
      assertThat("After manual set results in UTF-8 character set", feb.getCharacterSet(), is("ISO"));
      assertThat("After manual add of value results in \"<1> Form Encoded values: [\"Test\"], [\"Value\"\"] as its getLogString result", feb.getLogString(), is("<1> Form Encoded values: [\"Test\"], [\"Value\"]"));
      
      List<NameValuePair> vals = new ArrayList<>();
      vals.add(new BasicNameValuePair("Test", "Value"));
      feb = new FormEncodedBody(vals, "ISA");
      assertThat("Using iterable constructor value results in \"<1> Form Encoded values: [\"Test\"], [\"Value\"\"] as its getLogString result", feb.getLogString(), is("<1> Form Encoded values: [\"Test\"], [\"Value\"]"));
      assertThat("After manual set results in UTF-8 character set", feb.getCharacterSet(), is("ISA"));
      feb = new FormEncodedBody(vals);
      assertThat("Using iterable constructor value results in \"<1> Form Encoded values: [\"Test\"], [\"Value\"\"] as its getLogString result", feb.getLogString(), is("<1> Form Encoded values: [\"Test\"], [\"Value\"]"));
            
      Map<String, String> map = new HashMap<>();
      map.put("Test", "Value");
      feb = new FormEncodedBody(map, "ISA");
      assertThat("Using iterable constructor value results in \"<1> Form Encoded values: [\"Test\"], [\"Value\"\"] as its getLogString result", feb.getLogString(), is("<1> Form Encoded values: [\"Test\"], [\"Value\"]"));
      assertThat("After manual set results in UTF-8 character set", feb.getCharacterSet(), is("ISA"));
      feb = new FormEncodedBody(map);
      assertThat("Using iterable constructor value results in \"<1> Form Encoded values: [\"Test\"], [\"Value\"\"] as its getLogString result", feb.getLogString(), is("<1> Form Encoded values: [\"Test\"], [\"Value\"]"));
      
      assertThat("entity is a URLFormEncodedEntity", feb.getEntity() instanceof UrlEncodedFormEntity, is(true));
      Assertions.assertThrows(BJADWebException.class, () -> {
         new FormEncodedBody(map, "WAKAWAKA").getEntity();
       }, "Should throw BJADWebException with bad encoding passed");
   }
   
   /**
    * Tests the file upload body
    * 
    * @throws Exception
    *    Any unhandled exceptions will be thrown, causing the test to fail
    */
   @Test
   public void testFileBody() throws Exception
   {
      FileUploadBody<byte[]> byteBody = new FileUploadBody<byte[]>(new byte[] {0,1,2,3});
      assertThat("Body is not null", byteBody.getBody(), notNullValue());
      assertThat("Byte[] getEntity returns ByteArrayEntity", byteBody.getEntity() instanceof ByteArrayEntity, is(true));
      assertThat("Byte[] getLogString returns proper string", byteBody.getLogString(), is("Body being applied with a Byte array (\"4 bytes in length\")"));
      
      byteBody = new FileUploadBody<byte[]>(MediaType.BINARY, new byte[] {0,1,2,3});
      assertThat("Byte[] toString returns proper string", byteBody.toString(), is("Body being applied with a Byte array (\"4 bytes in length\") with media type application/octet-stream"));
      assertThat("Byte[] getEntity returns ByteArrayEntity", byteBody.getEntity() instanceof ByteArrayEntity, is(true));
      
      File f = new File("Test.xml");
      FileUploadBody<File> fileBody = new FileUploadBody<File>(f);
      assertThat("File getLogString returns proper string", fileBody.getLogString(), is("Body being applied with a File (\"" + f.getAbsolutePath() + "\")"));
      assertThat("File getEntity returns FileEntity", fileBody.getEntity() instanceof FileEntity, is(true));
      
      fileBody = new FileUploadBody<File>(MediaType.BINARY, f);
      assertThat("File toString returns proper string", fileBody.toString(), is("Body being applied with a File (\"" + f.getAbsolutePath() + "\") with media type application/octet-stream"));
      assertThat("File getEntity returns FileEntity", fileBody.getEntity() instanceof FileEntity, is(true));
      
      try (InputStream i = ClassLoader.getSystemResourceAsStream("Baseset.properties"))
      {
         FileUploadBody<InputStream> isBody = new FileUploadBody<InputStream>(i);
         assertThat("InputStream getLogString returns proper string", isBody.getLogString(), is("Body being applied with a InputStream"));
         assertThat("InputStream getEntity returns InputStreamEntity", isBody.getEntity() instanceof InputStreamEntity, is(true));
      }
      
      try (InputStream i = ClassLoader.getSystemResourceAsStream("Baseset.properties"))
      {
         FileUploadBody<InputStream> isBody = new FileUploadBody<InputStream>(MediaType.BINARY, i);
         assertThat("InputStream toString returns proper string", isBody.toString(), is("Body being applied with a InputStream with media type application/octet-stream"));
         assertThat("InputStream getEntity returns InputStreamEntity", isBody.getEntity() instanceof InputStreamEntity, is(true));
      }
      
      FileUploadBody<byte[]> noBody = new FileUploadBody<byte[]>(null);
      assertThat("Null Byte[] getLogString returns proper string", noBody.getLogString(), is("Body being applied with a no body data"));
      
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
         new FileUploadBody<byte[]>(null).getEntity();
       }, "Should throw IllegalArgumentException with null body");
      
      FileUploadBody<Integer> intBody = new FileUploadBody<Integer>(100);
      assertThat("Integer getLogString returns proper string", intBody.getLogString(), is("Body being applied with a unsupported data type (java.lang.Integer)"));
      
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
         new FileUploadBody<Integer>(100).getEntity();
       }, "Should throw IllegalArgumentException with int body type");
   }
}

