package bjad.web.properties;

import java.util.Properties;
import java.util.Map.Entry;

/**
 * Environment variable replacer that will get 
 * environment variables and their values to 
 * get placed in the strings being generated. 
 *
 * @author 
 *  Ben Dougall
 */
public class EnviromentVarReplacer extends AbstractPropReplacer
{
   private boolean caseInsenitive; 
   
   /**
    * @param caseInsenitive
    *    True to match against environment variables regardless
    *    of case (slower), false to match with exact case.
    */
   public EnviromentVarReplacer(boolean caseInsenitive)
   {
      super(caseInsenitive ? "env.ci." : "env.");
      this.caseInsenitive = caseInsenitive;
   }

   /**
    * @see bjad.web.properties.AbstractPropReplacer#getPropertyReplacementText(java.lang.String, java.util.Properties)
    */
   @Override
   public String getPropertyReplacementText(String variableName, Properties sourceProps)
   {
      String retVal = "";
      if (caseInsenitive)
      {         
         for (Entry<String, String> env : System.getenv().entrySet())
         {
            if (env.getKey().equalsIgnoreCase(variableName))
            {
               retVal = env.getValue();
               break;
            }
         }
      }
      else
      {
         retVal = System.getenv(variableName);
         retVal = retVal == null ? "" : retVal;
      }
      return retVal;
   }
   
}
