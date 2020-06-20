package bjad.web.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;

import bjad.web.BJADWebConstants;
import bjad.web.BJADWebException;

/**
 * <p>
 * Utility to load properties into a common collection, 
 * including the ability to load property files defined 
 * within the properties loaded. 
 * <p>
 * When loading properties, any properties loaded
 * with the AdditionalPropertyFile.# will be loaded using 
 * prefix of the property file. The prefixes are:
 * <ul>
 *    <li><b>classpath:</b> : File within the classpath</li>
 *    <li><b>file:</b> : File within the file system</li>
 * </ul>
 * <p>
 * The properties outside of the additional file items can 
 * be set to load property values within the collection, 
 * the environment variables, and the jvm system properties.
 * These values can be expressed using the following options, activated
 * in the order they appear.
 * <ul>     
 *    <li><b>${env.ci.[propertyName]}</b> : Environment variable, case insensitive</li>
 *    <li><b>${env.[propertyName]}</b> : Environment variable, case sensitive</li>
 *    <li><b>${jvm.ci.[propertyName]}</b> : JVM system property, case insensitive</li>
 *    <li><b>${jvm.[propertyName]}</b> : JVM system property, case sensitive</li>
 *    <li><b>${ci.[propertyName]}</b> : Property in the collection, case insensitive</li>
 *    <li><b>${[propertyName]}</b> : Property in the collection, case sensitive</li>
 * </ul>
 *
 * @author 
 *    Ben Dougall
 */
public class EnhancedPropertyHelper
{
   /**
    * The array of replacers to the run through when obtaining the 
    * properties and their internal replacement markers.
    */
   protected static AbstractPropReplacer[] replacers = new AbstractPropReplacer[]
         {
               new EnviromentVarReplacer(true),
               new EnviromentVarReplacer(false),
               new JvmVarReplacer(true),
               new JvmVarReplacer(false),
               new InternalPropertyReplacer(true),
               new InternalPropertyReplacer(false),
         };
   
   /**
    * The logger to use within this class.
    */
   protected Log logger; 
   /**
    * The properties loaded from all the files 
    * loaded by this class.
    */
   protected Properties properties = new Properties(); 
   
   /**
    * The list of resources loaded to the same file is 
    * not loaded over and over by this property loader.
    */
   protected List<String> loadedResources = new ArrayList<String>();
   
   /**
    * String used to adjust the logging to say initial or  
    * additional when loading the properties.
    */
   private String loadType = "initial";
   
   /**
    * Default constructor, setting the logger 
    * to the logger defined by the current
    * class name.
    */
   public EnhancedPropertyHelper()
   {
      logger = setLoggerFromName("");
   }
   
   /**
    * Creates the converter with the logger set from
    * a string. 
    *
    * @param loggerName
    *    The logger name to use, or if blank,
    *    the current class name will be used.
    */
   public EnhancedPropertyHelper(String loggerName)
   {
      logger = setLoggerFromName(loggerName);
   }
   
   /**
    * Create the converter with the logger set using 
    * the class definition provided. 
    *
    * @param loggerClass
    *    The class to use to make the logger.
    */
   public EnhancedPropertyHelper(Class<?> loggerClass)
   {
      logger = setLoggerFromName(loggerClass.getCanonicalName());
   }
   
   /**
    * Creates the converter using the log implementation
    * passed. 
    *
    * @param log
    *    The logger to use.
    */
   public EnhancedPropertyHelper(Log log)
   {
      logger = log;
   }
   
   /**
    * Loads the properties from the input stream provided
    * and processes the properties for additional property
    * files to load. 
    * 
    * @param is
    *    The input stream to load the properties from.
    * @throws BJADWebException
    *    Any exceptions creating the properties object 
    *    from the input stream will be thrown.
    */
   public void loadProperties(InputStream is) throws BJADWebException
   {
      try
      {
         Properties p = new Properties();
         p.load(is);
         logger.info("Loading " + loadType + " properties from input stream, type: " + is.getClass().getCanonicalName());
         loadPropertiesIntoCollection(p);
      }
      catch (IOException ex)
      {
         throw new BJADWebException("Failed to load properties from the input stream", ex);
      }
   }
   
   /**
    * Loads the properties from the File provided
    * and processes the properties for additional property
    * files to load. 
    * 
    * @param propFile
    *    The file object representing the file to load.
    * @throws BJADWebException
    *    Any exceptions creating the properties object 
    *    from the input stream will be thrown.
    */
   public void loadProperties(File propFile) throws BJADWebException
   {
      if (!loadedResources.contains(("FILE:"+propFile.getAbsolutePath()).toUpperCase()))
      {
         try
         {
            logger.info("Loading " + loadType + " properties from file: " + propFile.getAbsolutePath());
            try (InputStream is = new FileInputStream(propFile))
            {            
               Properties p = new Properties();
               p.load(is);
               loadPropertiesIntoCollection(p);
               
               loadedResources.add(("FILE:"+propFile.getAbsolutePath()).toUpperCase());
            }
         }
         catch (IOException ex)
         {
            throw new BJADWebException("Failed to load properties from " + propFile.getAbsolutePath(), ex);
         }
      }
      else 
      {
         logger.info("Skipping the reload of file: " + propFile.toString());
      }
   }
   
   /**
    * Applies the properties within the properties passed, adding them 
    * to the collection of properties generated within this class. Also
    * processes the properties to see if there are any other property 
    * files to load. 
    * 
    * @param newProperties
    *    The properties to apply to the current object, and process 
    *    for the additional data files to find. 
    * @throws BJADWebException 
    *    Any exceptions in loading the property files stated in the initial
    *    data load will thrown.
    */
   public void loadProperties(Properties newProperties) throws BJADWebException
   {
      logger.info("Loading " + loadType + " " + newProperties.size() + " precanned properties.");
      loadPropertiesIntoCollection(newProperties);
   }
   
   /**
    * Loads the properties from the class path resource file provided
    * and processes the properties for additional property
    * files to load. 
    * 
    * @param pathWithinClasspath
    *    The path within the classpath to load the resource file
    *    from.
    * @throws BJADWebException
    *    Any exceptions creating the properties object 
    *    from the input stream will be thrown.
    */
   public void loadPropertiesFromClasspathFile(String pathWithinClasspath) throws BJADWebException
   {
      if (!loadedResources.contains(("CP:"+pathWithinClasspath).toUpperCase()))
      {
         logger.info("Loading " + loadType + " properties from classpath resource: " + pathWithinClasspath);
         try (InputStream is = ClassLoader.getSystemResourceAsStream(pathWithinClasspath))
         {
            Properties p = new Properties();
            p.load(is);
            loadPropertiesIntoCollection(p);
            
            loadedResources.add(("CP:"+pathWithinClasspath).toUpperCase());
         }
         catch (IOException ex)
         {
            throw new BJADWebException("Failed to load properties from class path resource: " + pathWithinClasspath, ex);
         }
      }
      else
      {
         logger.info("Skipping the reload of classpath resource: " + pathWithinClasspath);
      }
   }
   
   /**
    * Applies the properties within the properties passed, adding them 
    * to the collection of properties generated within this class. Also
    * processes the properties to see if there are any other property 
    * files to load. 
    * 
    * @param newProperties
    *    The properties to apply to the current object, and process 
    *    for the additional data files to find. 
    * @throws BJADWebException 
    *    Any exceptions in loading the property files stated in the initial
    *    data load will thrown.
    */
   protected void loadPropertiesIntoCollection(Properties newProperties) throws BJADWebException
   {
      loadType = "additional";
      for (Entry<Object, Object> entry : newProperties.entrySet())
      {         
         String propName = entry.getKey().toString().toLowerCase().trim();
         if (propName.startsWith(BJADWebConstants.PROPERTY_ADDITIONAL_PROPERTY_FILE_PREFIX.toLowerCase()))
         {  
            String propValue = entry.getValue().toString().trim();
            
            if (propValue.toLowerCase().startsWith(BJADWebConstants.ADDITIONAL_FILE_CLASSPATH_PREFIX))
            {
               try
               {
                  String filePath = propValue.substring(BJADWebConstants.ADDITIONAL_FILE_CLASSPATH_PREFIX.length()).trim();                  
                  loadPropertiesFromClasspathFile(filePath);
               }
               catch (Exception ex)
               {
                  throw new BJADWebException("Failed to load properties from class path resource: " + propValue);
               }
            }
            if (propValue.toLowerCase().startsWith(BJADWebConstants.ADDITIONAL_FILE_FILE_PREFIX))
            {
               try
               {
                  File file = new File(propValue.substring(BJADWebConstants.ADDITIONAL_FILE_FILE_PREFIX.length()).trim());
                  loadProperties(file);
               }
               catch (Exception ex)
               {
                  throw new BJADWebException("Failed to load properties from file path: " + propValue);
               }
            }
         }
         else
         {
            logger.trace("Adding property: " + entry.getKey() + ", " + entry.getValue());
            properties.put(entry.getKey(), entry.getValue());
         }
      }
   }
   
   /**
    * Returns the logger using the string passed.
    * If the string is blank or null, the name 
    * of the current class will be used.
    * 
    * @param name
    *    The name of the logger.
    * @return
    *    The logger from the name passed.
    */
   protected Log setLoggerFromName(String name)
   {
      return LogFactory.getLog(TextUtils.isBlank(name)? this.getClass().getCanonicalName() : name);
   }
   
   /**
    * Returns the value of the property name passed, or null
    * if the property is not found.
    * 
    * @param name
    *    The property to get.
    * @return
    *    The value of the property name passed, null if its not found.
    */
   public String getPropertyValue(String name)
   {
      return transformValue(properties.getProperty(name));
   }
   
   /**
    * Returns the value of the property name passed, or the default
    * value if the property is not found.
    * 
    * @param name
    *    The property to get.
    * @param defaultValue
    *    The default value to return if the property is not found.
    * @return
    *    The value of the property name passed, default value if its not found.
    */
   public String getPropertyValue(String name, String defaultValue)
   {
      return transformValue(properties.getProperty(name, defaultValue));
   }
   
   /**
    * Returns the BigDecimal value of the property name passed, or 
    * BigDecimal.ZERO if the property is missing or is not a numeric
    * value.
    * 
    * @param name
    *    The property to get.
    * @return
    *    The numeric value of the property name passed, or  
    *    BigDecimal.ZERO if the property is missing or not a number.
    */
   public BigDecimal getNumericValue(String name)
   {
      return getNumericValue(name, BigDecimal.ZERO);
   }
   /**
    * Returns the BigDecimal value of the property name passed, or the 
    * default value if the property is not found.
    * 
    * @param name
    *    The property to get.
    * @param defaultValue
    *    The default value to return if the property is not found or
    *    is not a number.
    * @return
    *    The numeric value of the property name passed, or the default 
    *    value if the property is missing or not a number.
    */
   public BigDecimal getNumericValue(String name, BigDecimal defaultValue)
   {
      BigDecimal retValue = defaultValue;
      String val = getPropertyValue(name);
      try
      {         
         if (!TextUtils.isBlank(val))
         {
            retValue = new BigDecimal(val.replace(",", "").replace(" ", ""));
         }
      }
      catch (Exception ex)
      {
         logger.warn(name + "'s property value of \"" + val + "\" could not be made into a number");
      }
      return retValue;
   }
   
   /** 
    * Returns the list of property keys within the collection.
    * 
    * @return
    *    The list of string keys within the property collection.
    */
   public List<String> getPropertyKeys()
   {
      List<String> keys = new ArrayList<>();
      for (Object key : properties.keySet())
      {
         keys.add(key.toString());
      }
      return keys;
   }
   
   /**
    * Transforms the property values based on any internal
    * properties within their value, defined by ${propertyname}
    * 
    * @param originalValue
    *    The original property value.
    * @return
    *    The transformed property value, with any internal 
    *    properties replaced with the other property 
    *    values.
    */
   private String transformValue(String originalValue)
   {
      if (originalValue == null)
      {
         return originalValue;
      }
      int index = 0;
      String value = originalValue;
      for (;;)
      {
         index = value.indexOf("${", index);
         if (index == -1)
         {
            break;
         }
         int endPos = value.indexOf('}', index);
         if (endPos != -1)
         {
            String toReplace = value.substring(index + 2, endPos).trim();            
            String propertyValue = "";
            
            for (AbstractPropReplacer replacer : replacers)
            {
               if (toReplace.startsWith(replacer.getPrefix()))
               {                  
                  propertyValue = replacer.getPropertyReplacementText(toReplace.substring(replacer.getPrefix().length()), this.properties);
                  break;
               }               
            }
            
            logger.debug("Replacing: ${" + toReplace + "} with \"" + propertyValue + "\"");
            value = value.replace("${" + toReplace + "}", propertyValue);
         }
         index = index+2;
      }
      return value;
   }
}
