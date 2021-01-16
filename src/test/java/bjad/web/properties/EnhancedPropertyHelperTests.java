package bjad.web.properties;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import bjad.web.BJADWebException;

/**
 * Unit tests for the PropertiesToRequestConverter
 * class.   
 *
 * @author 
 *  Ben Dougall
 */
public class EnhancedPropertyHelperTests
{
   /**
    * Tests the property file loading from additional
    * file, which loads the base file.
    * 
    * @throws Exception
    *    Any exceptions will be thrown and fail the unit test.
    */
   @Test
   public void testBasicInheritance() throws Exception
   {
      EnhancedPropertyHelper converter = new EnhancedPropertyHelper(this.getClass());
      Properties p = new Properties();
      p.put("AdditionalPropertyFile.1", "file:src\\test\\resources\\AdditionalFile.properties");
      p.put("AdditionalPropertyFile.2", "file:src\\test\\resources\\AdditionalFile.properties");
      converter.loadProperties(p);
      assertThat("BaseProp ends up with the additional properties value", 
            converter.getPropertyValue("BaseProp"), 
            is("BaseProperty"));
      assertThat("FakeProp ends up with the additional properties value", 
            converter.getPropertyValue("FakeProp"), 
            is("additionalFileTest"));
      assertThat("FakeProp1 ends up with the additional properties value", 
            converter.getPropertyValue("FakeProp1"), 
            is("additional1"));
      assertThat("FakeProp2 ends up with the additional properties value", 
            converter.getPropertyValue("FakeProp2"), 
            is("dddREPLACEDsss"));
      assertThat("JavaVersion ends up with the additional properties value", 
            converter.getPropertyValue("JavaVersion"), 
            is(System.getProperty("java.runtime.version")));
      assertThat("CIJavaVersion ends up with the additional properties value", 
            converter.getPropertyValue("CIJavaVersion"), 
            is(System.getProperty("java.runtime.version")));
      assertThat("InternalCITest ends up with the CI property", 
            converter.getPropertyValue("InternalCITest"), 
            is("ssInternaloo"));
      assertThat("FakeENVTest ends up with blank value", 
            converter.getPropertyValue("FakeENVTest"), 
            is(""));
      assertThat("Bad number value ends up with BigDecimal.ZERO by default",
            converter.getNumericValue("FakeENVTest"),
            is(BigDecimal.ZERO));
      assertThat("Bad number value ends up with BigDecimal.ZERO by default",
            converter.getNumericValue("CIJavaVersion"),
            is(BigDecimal.ZERO));
   }
   
   /**
    * Given the OS differences for environment variables, this test
    * will generate a bunch of environment variable properties to test
    * its retrieval.
    * 
    * @throws Exception
    *    Any exceptions will be thrown.
    */
   @Test
   public void testEnvironmentVariableRetrieval() throws Exception
   {
      Map<String, String> envVars = System.getenv();
      int index = 0; 
      Iterator<String> keyIter = envVars.keySet().iterator();
      
      Log logger = LogFactory.getLog(this.getClass());
      
      while (keyIter.hasNext())
      {
         String key = keyIter.next();
         Properties p = new Properties();
         
         if (index % 2 == 0)
         {
            String value = envVars.get(key);
            p.put(key+index+"a", "${env.ci." + key.toLowerCase() + "}");
            p.put(key+index+"b", "${env.ci." + key.toUpperCase() + "}");
            EnhancedPropertyHelper propHelper = new EnhancedPropertyHelper(this.getClass());
            propHelper.loadProperties(p);
            
            assertThat("Environment variable using \"${env.ci." + key.toLowerCase() + "}\" gets the environment variable value for " + key, 
                  propHelper.getPropertyValue(key+index+"a"), 
                  is(value));
            logger.info(String.format("Verified %s got the \"%s\" environment variable value of \"%s\"", 
                  "${env.ci." + key.toLowerCase() + "}",
                  key,
                  value));            
            
            assertThat("Environment variable using \"${env.ci." + key.toUpperCase() + "}\" gets the environment variable value for " + key, 
                  propHelper.getPropertyValue(key+index+"b"), 
                  is(value));
            logger.info(String.format("Verified %s got the \"%s\" environment variable value of \"%s\"", 
                  "${env.ci." + key.toUpperCase() + "}",
                  key,
                  value));
         }
         else 
         {
            String value = envVars.get(key);
            p.put(key+index+"a", "${env." + key + "}");
            EnhancedPropertyHelper propHelper = new EnhancedPropertyHelper(this.getClass());
            propHelper.loadProperties(p);
            
            assertThat("Environment variable using \"${env." + key + "}\" gets the environment variable value for " + key, 
                  propHelper.getPropertyValue(key+index+"a"), 
                  is(value));
            logger.info(String.format("Verified %s got the \"%s\" environment variable value of \"%s\"", 
                  key+index+"a",
                  key,
                  value));
         }
         
         index++;
      }
   }
   
   /**
    * Getting the coverage for the log constructor.
    */
   @Test
   public void testLogConstructor()
   {
      Log l = null;
      new EnhancedPropertyHelper(l);
      
      new EnhancedPropertyHelper("loggerName");
      
      // No exceptions = pass
   }
   
   /**
    * Testing the bad file condition.
    */
   @Test
   public void testBadFileLoad()
   {
      EnhancedPropertyHelper e = new EnhancedPropertyHelper();
      try
      {
         e.loadProperties(new File("FakeFile.fake"));
         Assertions.fail("Fake file should have thrown exception.");
      }
      catch (BJADWebException ex)
      {
         assertThat("Exception for bad file is a IOException", ex.getCause() instanceof IOException, is(true));
      }
      
      try
      {
         EnhancedPropertyHelper converter = new EnhancedPropertyHelper(this.getClass());
         Properties p = new Properties();
         p.put("AdditionalPropertyFile.1", "file:src\\test\\resources\\fakeFile.properties");
         converter.loadProperties(p);
         Assertions.fail("Bad additional file resource should cause exception");
      }
      catch (Exception ex)
      {
      }
   }
   
   /**
    * Tests the input stream loading
    */
   @Test
   public void testInputStreamLoad()
   {
      EnhancedPropertyHelper e = new EnhancedPropertyHelper();
      try (InputStream is = ClassLoader.getSystemResourceAsStream("BaseSet.properties"))
      {
         e.loadProperties(is);
      }
      catch (Exception ex)
      {
         Assertions.fail("Input Stream load should not have thrown " + ex.getClass().getSimpleName() + " whose message was " + ex.getMessage());
      }
      
      try (InputStream is = ClassLoader.getSystemResourceAsStream("BaseSet.properties"))
      {
         is.close();
         e.loadProperties(is);
         Assertions.fail("Loading a closed input stream should have caused exception.");
      }
      catch (Exception ex)
      {
         
      }
   }
   
   /**
    * Tests the various conditions for the class path resource loading
    */
   @Test
   public void testClasspathLoading()
   {
      EnhancedPropertyHelper e = new EnhancedPropertyHelper();      
      try
      {
         e.loadPropertiesFromClasspathFile("Baseset.properties");
         e.loadPropertiesFromClasspathFile("Baseset.properties");
      } 
      catch (BJADWebException ex)
      {
         Assertions.fail("Double Classpath load should not have thrown " + ex.getClass().getSimpleName() + " whose message was " + ex.getMessage());
      }      
      
      e = new EnhancedPropertyHelper();
      try
      {
         e.loadPropertiesFromClasspathFile("FakeFile.fake");
         Assertions.fail("Fake file should have thrown exception.");
      }
      catch (BJADWebException ex)
      {
         assertThat("Exception for bad classpath file is a IOException", ex.getCause() instanceof IOException, is(true));
      }
      
      try
      {
         EnhancedPropertyHelper converter = new EnhancedPropertyHelper(this.getClass());
         Properties p = new Properties();
         p.put("AdditionalPropertyFile.1", "classpath:fakeFile.properties");
         converter.loadProperties(p);
         Assertions.fail("Bad additional classpath resource should cause exception");
      }
      catch (Exception ex)
      {
         
      }
   }
}
