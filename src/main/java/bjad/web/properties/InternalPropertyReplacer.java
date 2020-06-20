package bjad.web.properties;

import java.util.Properties;
import java.util.Map.Entry;

/**
 * Internal property replacer that will get 
 * internal properties and their values to 
 * get placed in the strings being generated. 
 *
 * @author 
 *  Ben Dougall
 */
public class InternalPropertyReplacer extends AbstractPropReplacer
{
   private boolean caseInsenitive; 
   
   /**
    * @param caseInsenitive
    *    True to match against environment variables regardless
    *    of case (slower), false to match with exact case.
    */
   public InternalPropertyReplacer(boolean caseInsenitive)
   {
      super(caseInsenitive ? "ci." : "");
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
         for (Entry<Object, Object> jvmVar : sourceProps.entrySet())
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
         retVal = sourceProps.getProperty(propertyName, "");
      }
      return retVal;
   }
   
}
