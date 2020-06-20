package bjad.web.body;

import org.apache.http.HttpEntity;

import bjad.web.BJADWebException;

/**
 * Abstract body model that will provide the 
 * common method to create the HTTPEntity
 * within the Web Component.
 *
 * @author 
 *  Ben Dougall
 */
public abstract class AbstractBodyModel
{
   /**
    * Provides the apache httpentity instance 
    * for the body the model represents.  
    * 
    * @return
    *    The httpentity from the apache http
    *    library for the body represented by
    *    this library.
    * @throws BJADWebException
    *    Any exceptions while creating the apache 
    *    http entity will be thrown as a BJADWebException. 
    */
   public abstract HttpEntity getEntity() throws BJADWebException;
   
   /**   
    * Provides the string to log for the 
    * body the object represents
    * 
    * @return
    *    string that represents the body the object contains. 
    */
   public abstract String getLogString();
}
