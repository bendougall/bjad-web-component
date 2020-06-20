package bjad.web.properties;

import java.util.Properties;

/**
 * Abstract property text replacer.
 *
 * @author 
 *  Ben Dougall
 */
public abstract class AbstractPropReplacer
{
   /**
    * The prefix of the internal property text to 
    * match against in order for this replacer 
    * to activate.
    */
   protected String prefix;
   
   /**
    * Constructor, setting the prefix to search for.
    *
    * @param prefix
    *    The prefix to search for in order for 
    *    this replacer to operate.
    */
   public AbstractPropReplacer(String prefix)
   {
      this.prefix = prefix;
   }
   
   /**
    * Provides the prefix this replacer activates for.
    * 
    * @return
    *    The prefix this replacer activates for.
    */
   public String getPrefix()
   {
      return this.prefix;
   }
   
   /**
    * Gets the text to replace the property text in the 
    * string with.
    * 
    * @param propertyName
    *    The name of the property to get from the replacer's
    *    collection (env, jvm, or internal properties) 
    * @param sourceProps
    *    The internal property collection
    * @return
    *    The value to use within the text being generated. if
    *    the property cannot be found, a blank string will be 
    *    returned.
    */
   public abstract String getPropertyReplacementText(String propertyName, Properties sourceProps);
}
