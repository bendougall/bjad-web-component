package bjad.web;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import bjad.web.properties.EnhancedPropertyHelper;

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
      assertThat("MachineName ends up with the additional properties value", 
            converter.getPropertyValue("MachineName"), 
            is(System.getenv("COMPUTERNAME")));
      assertThat("JavaVersion ends up with the additional properties value", 
            converter.getPropertyValue("JavaVersion"), 
            is(System.getProperty("java.runtime.version")));
      assertThat("CIMachineName ends up with the additional properties value", 
            converter.getPropertyValue("CIMachineName"), 
            is(System.getenv("COMPUTERNAME")));
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
}
