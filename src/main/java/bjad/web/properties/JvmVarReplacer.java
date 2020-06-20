package bjad.web.properties;

import java.util.Properties;
import java.util.Map.Entry;

/**
 * JVM property replacer that will get 
 * JVM properties and their values to 
 * get placed in the strings being 
 * generated. 
 *
 * @author 
 *  Ben Dougall
 */
public class JvmVarReplacer extends AbstractPropReplacer
{
   private boolean caseInsenitive; 
   
   /**
    * @param caseInsenitive
    *    True to match against environment variables regardless
    *    of case (slower), false to match with exact case.
    */
   public JvmVarReplacer(boolean caseInsenitive)
   {
      super(caseInsenitive ? "jvm.ci." : "jvm.");
      this.caseInsenitive = caseInsenitive;
   }

   /**
    * @see bjad.web.properties.AbstractPropReplacer#getPropertyReplacementText(java.lang.String, java.util.Properties)
    */
   @Override
   public String getPropertyReplacementText(String propertyName, Properties sourceProps)
   {
      String retVal = "";
      if (caseInsenitive)
      {         
         for (Entry<Object, Object> jvmVar : System.getProperties().entrySet())
         {
            if (jvmVar.getKey().toString().equalsIgnoreCase(propertyName))
            {
               retVal = jvmVar.getValue().toString();
               break;
            }
         }
      }
      else
      {
         retVal = System.getProperty(propertyName, "");
      }
      return retVal;
   }
   
}
